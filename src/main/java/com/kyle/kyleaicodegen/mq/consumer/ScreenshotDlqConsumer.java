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
    public void handleDlqMessage(ScreenshotMessage message,
                                 @Header(value = "x-death", required = false) List<Map<String, Object>> deaths) {
        String messageId = message.getMassageId();
        if (messageId == null) {
            // 如果生产者没传 messageId，可以用 appId+url 生成一个哈希
            messageId = message.getAppId() + "_" + message.getDeployUrl().hashCode();
        }

        boolean exists = screenshotTaskDlqService.existsByMessageId(messageId);
        if (exists) {
            log.warn("重复消息已存在，跳过处理: messageId={}, message={}", messageId, message);
            return;
        }
        long retryCount = 0;
        if (deaths != null && !deaths.isEmpty()) {
            Object countObj = deaths.getFirst().get("count");
            if (countObj instanceof Number) {
                retryCount = ((Number) countObj).longValue();
            }
        }

        log.error("死信队列收到任务: {}，失败次数: {}", message, retryCount);

        ScreenshotTaskDlq entity = new ScreenshotTaskDlq();
        entity.setMessageId(messageId);
        entity.setAppId(message.getAppId());
        entity.setDeployUrl(message.getDeployUrl());
        entity.setErrorMessage("任务失败进入死信队列"); // 可以考虑附加原始错误信息
        entity.setRetryCount((int) retryCount);
        entity.setStatus(0);

        try {
            boolean save = screenshotTaskDlqService.save(entity);
            if (save) {
                log.info("死信队列任务已入库: {}", message);
            }
        } catch (Exception ex) {
            log.error("保存死信任务失败: {}", message, ex);
            // 避免消息反复消费，可以选择手动 ack/nack
            // 或者把异常抛出让消息再次进 DLQ，但要注意死循环
        }
    }
}

