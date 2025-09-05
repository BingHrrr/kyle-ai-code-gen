package com.kyle.kyleaicodegen.langgraph4j.node;

import com.kyle.kyleaicodegen.core.builder.VueProjectBuilder;
import com.kyle.kyleaicodegen.exception.BusinessException;
import com.kyle.kyleaicodegen.exception.ErrorCode;
import com.kyle.kyleaicodegen.langgraph4j.state.WorkflowContextMessage;
import com.kyle.kyleaicodegen.model.enums.CodeGenTypeEnum;
import com.kyle.kyleaicodegen.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;

import java.io.File;

import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * 项目构建节点
 * @author Haoran Wang
 */
@Slf4j
public class ProjectBuildNode {
    public static AsyncNodeAction<MessagesState<String>> create() {
        return node_async(state -> {
            WorkflowContextMessage context = WorkflowContextMessage.getContext(state);
            log.info("执行节点: 项目构建");

            // 获取必要的参数
            String generatedCodeDir = context.getGeneratedCodeDir();
            CodeGenTypeEnum generationType = context.getGenerationType();
            String buildResultDir;
            // Vue 项目类型：使用 VueProjectBuilder 进行构建
            if (generationType == CodeGenTypeEnum.VUE_PROJECT) {
                try {
                    VueProjectBuilder vueBuilder = SpringContextUtil.getBean(VueProjectBuilder.class);
                    // 执行 Vue 项目构建（npm install + npm run build）
                    boolean buildSuccess = vueBuilder.buildProject(generatedCodeDir);
                    if (buildSuccess) {
                        // 构建成功，返回 dist 目录路径
                        buildResultDir = generatedCodeDir + File.separator + "dist";
                        log.info("Vue 项目构建成功，dist 目录: {}", buildResultDir);
                    } else {
                        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Vue 项目构建失败");
                    }
                } catch (Exception e) {
                    log.error("Vue 项目构建异常: {}", e.getMessage(), e);
                    buildResultDir = generatedCodeDir; // 异常时返回原路径
                }
            } else {
                // HTML 和 MULTI_FILE 代码生成时已经保存了，直接使用生成的代码目录
                buildResultDir = generatedCodeDir;
            }
            
            // 更新状态
            context.setCurrentStep("项目构建");
            context.setBuildResultDir(buildResultDir);
            log.info("项目构建完成，结果目录: {}", buildResultDir);
            return WorkflowContextMessage.saveContext(context);
        });
    }
}

