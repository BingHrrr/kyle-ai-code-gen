package com.kyle.kyleaicodegen.mq.consumer;

import com.kyle.kyleaicodegen.model.entitiy.ScreenshotTaskDlq;
import com.kyle.kyleaicodegen.mq.config.RabbitMQConfig;
import com.kyle.kyleaicodegen.mq.pojo.ScreenshotMessage;
import com.kyle.kyleaicodegen.service.ScreenshotTaskDlqService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ScreenshotDlqConsumer {

    @Resource
    private ScreenshotTaskDlqService screenshotTaskDlqService;

    @RabbitListener(queues = RabbitMQConfig.SCREENSHOT_DLQ)
    public void handleDlqMessage(ScreenshotMessage message, @Header(value = "x-death", required = false) List<Map<String, Object>> deaths) {
        long retryCount = 0;
        if (deaths != null && !deaths.isEmpty()) {
            retryCount = (long) deaths.getFirst().get("count");
        }

        log.error("死信队列收到任务: {}，失败次数: {}", message, retryCount);
        ScreenshotTaskDlq entity = new ScreenshotTaskDlq();
        entity.setAppId(message.getAppId());
        entity.setDeployUrl(message.getDeployUrl());
        entity.setErrorMessage("任务失败进入死信队列");
        entity.setRetryCount((int) retryCount);
        entity.setStatus(0);
        boolean save = screenshotTaskDlqService.save(entity);
        if (save) {
            log.info("死信队列任务已入库: {}", message);
        }
    }
}
