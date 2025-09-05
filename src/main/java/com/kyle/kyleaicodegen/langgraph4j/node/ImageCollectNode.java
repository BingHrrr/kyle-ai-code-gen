package com.kyle.kyleaicodegen.langgraph4j.node;

import com.kyle.kyleaicodegen.langgraph4j.ai.ImageCollectService;
import com.kyle.kyleaicodegen.langgraph4j.state.WorkflowContextMessage;
import com.kyle.kyleaicodegen.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;

import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * @author Haoran Wang
 * @since 2025
 */

@Slf4j
public class ImageCollectNode {
    public static AsyncNodeAction<MessagesState<String>> create(){
        // 重写NodeAction的apply方法
        return node_async(state -> {
            WorkflowContextMessage context = WorkflowContextMessage.getContext(state);
            log.info("当前节点： 图片收集");
            String originalPrompt = context.getOriginalPrompt();
            String imageList = "";
            try {
                // 获取AI图片收集服务
                ImageCollectService imageCollectionService = SpringContextUtil.getBean(ImageCollectService.class);
                // 使用 AI 服务进行智能图片收集
                imageList = imageCollectionService.collectImages(originalPrompt);
                imageCollectionService.collectImages(originalPrompt);
            } catch (Exception e) {
                log.error("图片收集失败: {}", e.getMessage(), e);
            }

            context.setCurrentStep("图片收集");
            context.setImageListStr(imageList);
            log.info("图片收集完成...");
            return WorkflowContextMessage.saveContext(context);
        });
    }
}
