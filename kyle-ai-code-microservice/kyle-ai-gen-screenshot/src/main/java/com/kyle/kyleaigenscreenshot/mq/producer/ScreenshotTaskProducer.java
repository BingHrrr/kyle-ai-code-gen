package com.kyle.kyleaigenscreenshot.mq.producer;

import com.kyle.kyleaigenscreenshot.mq.config.RabbitMQConfig;
import com.kyle.kyleaigenscreenshot.mq.pojo.ScreenshotMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScreenshotTaskProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendScreenshotTask(Long appId, String deployUrl) {
        // 生成唯一消息ID
        String messageId = UUID.randomUUID().toString();

        ScreenshotMessage message = new ScreenshotMessage(appId, deployUrl,messageId);

        log.info("发送消息 ==> appId={}, url={}, messageId={}", appId, deployUrl, messageId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SCREENSHOT_EXCHANGE,
                RabbitMQConfig.SCREENSHOT_ROUTING_KEY,
                message
        );
    }
}
