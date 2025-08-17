package com.kyle.kyleaicodegen.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Haoran Wang
 * @since 2025
 */

@Configuration
public class AiCodeGenServiceFactory {

    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel streamingChatModel;

    /**
     * 创建并配置一个名为 aiCodeGeneratorService 的 Bean
     * 该 Bean 是一个 AI 代码生成服务，使用 AiServices 类进行构建
     *
     * @return 返回一个配置好的 AiCodeGenService 实例
     */
    @Bean
    public AiCodeGenService aiCodeGeneratorService() {
        return AiServices.builder(AiCodeGenService.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                .build();
    }
}

