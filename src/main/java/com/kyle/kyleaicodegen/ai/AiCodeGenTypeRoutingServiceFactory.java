package com.kyle.kyleaicodegen.ai;

import com.kyle.kyleaicodegen.utils.SpringContextUtil;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI代码生成类型路由服务工厂
 *
 * @author Haoran Wang
 * @since 2025
 */
@Slf4j
@Configuration
public class AiCodeGenTypeRoutingServiceFactory {

    /**
     * 创建AI代码生成类型路由服务实例
     * 每个AiService携带一个chatmodel 所以每次要创建新的AiService实例
     */
    public AiCodeGenTypeRoutingService createAiCodeGenTypeRoutingService() {
        ChatModel routingChatModel = SpringContextUtil.getBean("routingChatModelPrototype", ChatModel.class);
        return AiServices.builder(AiCodeGenTypeRoutingService.class)
                .chatModel(routingChatModel)
                .build();
    }

    /**
     * 默认提供一个bean 兼容老的逻辑
     * @return AiService实例
     */
    @Bean
    public AiCodeGenTypeRoutingService aiCodeGenTypeRoutingService(){
        return createAiCodeGenTypeRoutingService();
    }
}
