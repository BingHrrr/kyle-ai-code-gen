package com.kyle.kyleaicodegen.mq.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyle.kyleaicodegen.mq.config.RabbitMQConfig;
import com.kyle.kyleaicodegen.mq.pojo.ScreenshotMessage;
import com.kyle.kyleaicodegen.service.impl.AppServiceImpl;
import com.kyle.kyleaicodegen.service.impl.ScreenshotServiceImpl;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScreenshotTaskConsumer {

    private final ScreenshotServiceImpl screenshotService;
    private final AppServiceImpl appService;

    @RabbitListener(queues = RabbitMQConfig.SCREENSHOT_QUEUE)
    public void handleScreenshotMessage(ScreenshotMessage message,
                                        Channel channel,
                                        @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                                        @Header(value = "x-death", required = false)
                                            List<Map<String, Object>> deaths) throws IOException {
        try {
            log.info("收到消息: {}", message);
            // 执行截图逻辑
            screenshotService.generateAndUploadScreenshot(message.getDeployUrl());
            appService.updateAppCover(message.getAppId(), message.getDeployUrl());
            // 成功 手动ack
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("截图任务失败: {}", e.getMessage(), e);
            // 获取当前已重试次数
            long retryCount = 0;
            if (deaths != null && !deaths.isEmpty()) {
                retryCount = (long) deaths.getFirst().get("count");
            }
            if (retryCount >= 3) {
                log.warn("任务已失败 {} 次，进入死信队列: {}", retryCount, message);
                channel.basicPublish(RabbitMQConfig.SCREENSHOT_EXCHANGE, "screenshot.dlq",
                        null, new ObjectMapper().writeValueAsBytes(message));
                channel.basicAck(tag, false);
            } else {
                log.info("任务第 {} 次失败，进入重试队列: {}", retryCount + 1, message);
                channel.basicNack(tag, false, false); // nack 不重新入队
            }
        }
    }

}



