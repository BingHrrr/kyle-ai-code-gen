package com.kyle.kyleaicodegen.mq.producer;

import com.kyle.kyleaicodegen.mq.config.RabbitMQConfig;
import com.kyle.kyleaicodegen.mq.pojo.ScreenshotMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

;

@Service
@RequiredArgsConstructor
public class ScreenshotTaskProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendScreenshotTask(Long appId, String deployUrl) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SCREENSHOT_EXCHANGE,
                RabbitMQConfig.SCREENSHOT_ROUTING_KEY,
                new ScreenshotMessage(appId, deployUrl)
        );
    }
}
