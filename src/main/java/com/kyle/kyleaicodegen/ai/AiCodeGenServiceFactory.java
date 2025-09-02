package com.kyle.kyleaicodegen.ai;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kyle.kyleaicodegen.ai.tools.ToolManager;
import com.kyle.kyleaicodegen.exception.BusinessException;
import com.kyle.kyleaicodegen.exception.ErrorCode;
import com.kyle.kyleaicodegen.model.enums.CodeGenTypeEnum;
import com.kyle.kyleaicodegen.service.ChatHistoryService;
import com.kyle.kyleaicodegen.utils.SpringContextUtil;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author Haoran Wang
 * @since 2025
 */

@Configuration
@Slf4j
public class AiCodeGenServiceFactory {
    // 智能路由和普通对话都是chatModel 需要指定name
    @Resource(name = "openAiChatModel")
    private ChatModel chatModel;

    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    @Resource
    private ChatHistoryService chatHistoryService;

    @Resource
    private ToolManager toolManager;
    /**
     * AI 服务实例缓存
     * 缓存策略：
     * - 最大缓存 1000 个实例
     * - 写入后 30 分钟过期
     * - 访问后 10 分钟过期
     */
    private final Cache<String, AiCodeGenService> serviceCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(30))
            .expireAfterAccess(Duration.ofMinutes(10))
            .removalListener((key, value, cause) -> {
                log.debug("AI 服务实例被移除，appId: {}, 原因: {}", key, cause);
            })
            .build();

    /**
     * 使用caffeine缓存，避免每次都创建新的实例
     * 根据appId 获取对应的AiCodeGenService 【多例】
     * @param appId 应用ID
     * @return AiCodeGenService
     */
    public AiCodeGenService getAiCodeGenService(long appId) {
        return getAiCodeGenService(appId, CodeGenTypeEnum.HTML);
    }

    public AiCodeGenService getAiCodeGenService(long appId, CodeGenTypeEnum codeGenTypeEnum) {
        String cacheKey = buildCacheKey(appId, codeGenTypeEnum);
        return serviceCache.get(cacheKey, key -> createAiCodeGenService(appId, codeGenTypeEnum));
    }

    /**
     * 创建新的 AI 服务实例
     */
    private AiCodeGenService createAiCodeGenService(long appId, CodeGenTypeEnum codeGenType) {
        log.info("为 appId: {} 创建新的 AI 服务实例", appId);
        // 根据 appId 构建独立的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory
                .builder()
                .id(appId)
                .chatMemoryStore(redisChatMemoryStore)
                .maxMessages(50)
                .build();
        chatHistoryService.loadChatHistoryToMemory(appId, chatMemory, 20);
        return switch (codeGenType){
            case VUE_PROJECT ->{
                StreamingChatModel reasoningStreamingChatModel = SpringContextUtil.getBean("reasoningStreamingChatModelPrototype", StreamingChatModel.class);
                yield AiServices.builder(AiCodeGenService.class)
                        .chatModel(chatModel)
                        .streamingChatModel(reasoningStreamingChatModel)
                        .chatMemoryProvider(memoryId -> chatMemory)
                        .tools(toolManager.getAllTools())
                        .hallucinatedToolNameStrategy(toolExecutionRequest
                                -> ToolExecutionResultMessage.from(toolExecutionRequest, "没有此工具：" + toolExecutionRequest.name()))
                        .build();
            }
            case HTML, MULTI_FILE -> {
                StreamingChatModel openAiStreamingChatModel = SpringContextUtil.getBean("streamingChatModelPrototype", StreamingChatModel.class);
                yield AiServices.builder(AiCodeGenService.class)
                    .chatModel(chatModel)
                    .streamingChatModel(openAiStreamingChatModel)
                    .chatMemory(chatMemory)
                    .build();
            }
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型: " + codeGenType);
        };
    }

    public String buildCacheKey(Long appId, CodeGenTypeEnum codeGenType){
        return appId + "_" + codeGenType.getValue();
    }
    @Bean
    public AiCodeGenService AiCodeGenService() {
        return getAiCodeGenService(0L);
    }
}

