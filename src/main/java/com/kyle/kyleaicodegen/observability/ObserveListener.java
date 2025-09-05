package com.kyle.kyleaicodegen.observability;

import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.output.TokenUsage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * @author Haoran Wang
 * @since 2025
 */

@Component
@Slf4j
public class ObserveListener implements ChatModelListener {
    @Resource
    private MetricsCollector metricsCollector;

    private static final String START_TIME_KEY = "request_start_time";
    private static final String OBSERVE_CONTEXT_KEY = "observe_context";

    @Override
    public void onRequest(ChatModelRequestContext requestContext) {
        // 请求刚开始 设置时间 用于后续按分位数统计桶中数据（直方图）
        requestContext.attributes().put(START_TIME_KEY, Instant.now());
        ObserveContext context = ObserveContextHolder.getContext();
        requestContext.attributes().put(OBSERVE_CONTEXT_KEY, context);
        String appId = context.getAppId();
        String userId = context.getUserId();
        String modelName = requestContext.chatRequest().modelName();
        metricsCollector.recordRequestTimes(userId, appId, modelName, "started");
    }

    /**
     * 注意这里监听Response的线程和监听onRequest线程不是同一个 因此要通过ChatModelResponseContext来传递
     *
     * @param responseContext 响应上下文
     */
    @Override
    public void onResponse(ChatModelResponseContext responseContext) {
        Map<Object, Object> attributes = responseContext.attributes();
        ObserveContext observeContext = (ObserveContext) attributes.get(OBSERVE_CONTEXT_KEY);
        String appId = observeContext.getAppId();
        String userId = observeContext.getUserId();
        String modelName = responseContext.chatResponse().modelName();
        metricsCollector.recordRequestTimes(userId, appId, modelName, "success");
        // 记录响应时间
        recordResponseTime(attributes, userId, appId, modelName);
        // 记录Token消耗
        recordTokenUsage(responseContext, userId, appId, modelName);
    }

    @Override
    public void onError(ChatModelErrorContext errorContext) {
        ChatModelListener.super.onError(errorContext);
    }

    private void recordTokenUsage(ChatModelResponseContext responseContext, String userId, String appId, String modelName) {
        TokenUsage tokenUsage = responseContext.chatResponse().tokenUsage();
        if (tokenUsage != null) {
            metricsCollector.recordTokenUsage(userId, appId, modelName, "input", tokenUsage.inputTokenCount());
            metricsCollector.recordTokenUsage(userId, appId, modelName, "output", tokenUsage.outputTokenCount());
            metricsCollector.recordTokenUsage(userId, appId, modelName, "total", tokenUsage.totalTokenCount());
        }
    }

    private void recordResponseTime(Map<Object, Object> attributes, String userId, String appId, String modelName) {
        Instant startInstant = (Instant) attributes.get(START_TIME_KEY);
        Duration duration = Duration.between(startInstant, Instant.now());
        metricsCollector.recordResponseTime(userId, appId, modelName, duration);
    }
}
