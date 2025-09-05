package com.kyle.kyleaicodegen.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 收集器 用于收集各种统计指标
 * @author Haoran Wang
 * @since 2025
 */

@Slf4j
@Component
public class MetricsCollector {
    // 指标注册
    @Resource
    private MeterRegistry meterRegistry;

    // 若干缓存 防止统计错误
    private final ConcurrentHashMap<String, Counter> requestCounterMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Counter> errorCounterMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Counter> tokenCounterMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Timer> responseTimeMap = new ConcurrentHashMap<>();

    /**
     * 记录请求次数
     */
    public void recordRequestTimes(String userId, String appId, String modelName, String status){
        // 构造唯一key 保证统计最细粒度 方便后续聚合和计算
        String key = String.format("%s_%s_%s_%s", userId, appId, modelName, status);
        Counter counter = requestCounterMap.computeIfAbsent(key, k ->
                Counter.builder("ai_model_requests_total")
                        .description("AI模型请求次数")
                        .tag("userId", userId)
                        .tag("appId", appId)
                        .tag("modelName", modelName)
                        .tag("status", status)
                        .register(meterRegistry)
        );
        // counter(默认+1）
        counter.increment();
    }

    /**
     * 记录错误
     */
    public void recordError(String userId, String appId, String modelName, String errorMessage) {
        String key = String.format("%s_%s_%s_%s", userId, appId, modelName, errorMessage);
        Counter counter = errorCounterMap.computeIfAbsent(key, k ->
                Counter.builder("ai_model_errors_total")
                        .description("AI模型错误次数")
                        .tag("user_id", userId)
                        .tag("app_id", appId)
                        .tag("model_name", modelName)
                        .tag("error_message", errorMessage)
                        .register(meterRegistry)
        );
        counter.increment();
    }

    /**
     * 记录Token消耗
     */
    public void recordTokenUsage(String userId, String appId, String modelName,
                                 String tokenType, long tokenCount) {
        String key = String.format("%s_%s_%s_%s", userId, appId, modelName, tokenType);
        Counter counter = tokenCounterMap.computeIfAbsent(key, k ->
                Counter.builder("ai_model_tokens_total")
                        .description("AI模型Token消耗总数")
                        .tag("user_id", userId)
                        .tag("app_id", appId)
                        .tag("model_name", modelName)
                        .tag("token_type", tokenType)
                        .register(meterRegistry)
        );
        counter.increment(tokenCount);
    }

    /**
     * 记录响应时间
     */
    public void recordResponseTime(String userId, String appId, String modelName, Duration duration) {
        String key = String.format("%s_%s_%s", userId, appId, modelName);
        Timer timer = responseTimeMap.computeIfAbsent(key, k ->
                Timer.builder("ai_model_response_duration_seconds")
                        .description("AI模型响应时间")
                        .tag("user_id", userId)
                        .tag("app_id", appId)
                        .tag("model_name", modelName)
                        .register(meterRegistry)
        );
        timer.record(duration);
    }
}
