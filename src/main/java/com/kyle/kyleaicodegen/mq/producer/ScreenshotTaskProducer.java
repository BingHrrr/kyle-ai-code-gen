package com.kyle.kyleaicodegen.mq.producer;

import com.kyle.kyleaicodegen.mq.config.RabbitMQConfig;
import com.kyle.kyleaicodegen.mq.pojo.ScreenshotMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

;
@Slf4j
@Service
@RequiredArgsConstructor
public class ScreenshotTaskProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendScreenshotTask(Long appId, String deployUrl) {
        log.info("发送消息 ==> {}, {}", appId, deployUrl);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SCREENSHOT_EXCHANGE,
                RabbitMQConfig.SCREENSHOT_ROUTING_KEY,
                new ScreenshotMessage(appId, deployUrl)
        );
    }
}
