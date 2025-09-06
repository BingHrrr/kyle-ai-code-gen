package com.kyle.kyleaicodegen.mq.consumer;

import com.kyle.kyleaicodegen.mq.config.RabbitMQConfig;
import com.kyle.kyleaicodegen.mq.pojo.ScreenshotMessage;
import com.kyle.kyleaicodegen.service.impl.AppServiceImpl;
import com.kyle.kyleaicodegen.service.impl.ScreenshotServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScreenshotTaskConsumer {

    private final ScreenshotServiceImpl screenshotService;
    private final AppServiceImpl appService;

    @RabbitListener(queues = RabbitMQConfig.SCREENSHOT_QUEUE)
    @Retryable(
            retryFor = Exception.class, // 哪些异常触发重试
            maxAttempts = 3,// 最大重试次数
            backoff = @Backoff(delay = 2000, multiplier = 2) // 指数退避
    )
    public void handleScreenshotMessage(
            ScreenshotMessage message
    ) {
        String messageId = message.getMassageId();
        log.info("收到消息: {}, messageId={}", message, messageId);
        // 执行截图逻辑
        String cosUrl = screenshotService.generateAndUploadScreenshot(message.getDeployUrl());
        // 更新应用封面
        appService.updateAppCover(message.getAppId(), cosUrl);
        log.info("截图任务完成: {}", message);
    }

    @Recover
    public void recover(Exception e, ScreenshotMessage message) {
        // 超过最大重试次数，进入 DLQ
        log.error("任务重试多次仍失败，丢入死信队列: {}", message, e);
        throw new AmqpRejectAndDontRequeueException("超过最大重试次数", e);
    }

}



