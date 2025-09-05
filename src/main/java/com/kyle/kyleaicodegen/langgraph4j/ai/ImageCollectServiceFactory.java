package com.kyle.kyleaicodegen.langgraph4j.ai;

import com.kyle.kyleaicodegen.langgraph4j.tools.IllustrationTool;
import com.kyle.kyleaicodegen.langgraph4j.tools.ImageSearchTool;
import com.kyle.kyleaicodegen.langgraph4j.tools.LogoGenTool;
import com.kyle.kyleaicodegen.langgraph4j.tools.MermaidStructureGenTool;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ImageCollectServiceFactory {

    @Resource(name = "openAiChatModel")
    private ChatModel chatModel;

    @Resource
    private ImageSearchTool imageSearchTool;

    @Resource
    private IllustrationTool illustrationTool;

    @Resource
    private MermaidStructureGenTool mermaidStructureGenTool;

    @Resource
    private LogoGenTool logoGenTool;

    /**
     * 创建图片收集 AI 服务
     */
    @Bean
    public ImageCollectService createImageCollectionService() {
        return AiServices.builder(ImageCollectService.class)
                .chatModel(chatModel)
                .tools(
                        imageSearchTool,
                        illustrationTool,
                        mermaidStructureGenTool,
                        logoGenTool
                )
                .build();
    }
}
