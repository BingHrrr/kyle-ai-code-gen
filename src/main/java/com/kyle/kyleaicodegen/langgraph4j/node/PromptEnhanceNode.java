package com.kyle.kyleaicodegen.langgraph4j.node;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.kyle.kyleaicodegen.langgraph4j.model.ImageResource;
import com.kyle.kyleaicodegen.langgraph4j.state.WorkflowContextMessage;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.bsc.langgraph4j.prebuilt.MessagesState;

import java.util.List;

import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * 提示词增强节点
 * @author Haoran Wang
 */
@Slf4j
public class PromptEnhanceNode {
    public static AsyncNodeAction<MessagesState<String>> create() {
        return node_async(state -> {
            WorkflowContextMessage context = WorkflowContextMessage.getContext(state);
            log.info("执行节点: 提示词增强");

            String originalPrompt = context.getOriginalPrompt();
            String imageListStr = context.getImageListStr();
            List<ImageResource> imgList = context.getImageList();
            StringBuilder builder = new StringBuilder();
            builder.append(originalPrompt);
            if (CollUtil.isNotEmpty(imgList) || StrUtil.isNotBlank(imageListStr)) {
                builder.append("\n\n## 可用素材资源\n");
                builder.append("请在生成网站使用以下图片资源，将这些图片合理地嵌入到网站的相应位置中。\n");
                if (CollUtil.isNotEmpty(imgList)) {
                    for (ImageResource image : imgList) {
                        builder.append("- ")
                                .append(image.getCategory().getText())
                                .append("：")
                                .append(image.getDescription())
                                .append("（")
                                .append(image.getUrl())
                                .append("）\n");
                    }
                } else {
                    builder.append(imageListStr);
                }
            }
            String enhancedPrompt = builder.toString();
            // 更新状态
            context.setCurrentStep("提示词增强");
            context.setEnhancedPrompt(enhancedPrompt);
            log.info("提示词增强完成");
            return WorkflowContextMessage.saveContext(context);
        });
    }
}

