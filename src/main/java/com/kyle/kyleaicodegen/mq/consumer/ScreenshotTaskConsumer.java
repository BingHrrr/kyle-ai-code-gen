package com.kyle.kyleaicodegen.mq.consumer;

import com.kyle.kyleaicodegen.mq.config.RabbitMQConfig;
import com.kyle.kyleaicodegen.service.impl.AppServiceImpl;
import com.kyle.kyleaicodegen.service.impl.ScreenshotServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScreenshotTaskConsumer {

    private final ScreenshotServiceImpl screenshotService;
    private final AppServiceImpl appService;

    @RabbitListener(queues = RabbitMQConfig.SCREENSHOT_QUEUE)
    public void handleScreenshotTask(Map<String, Object> message) {
        Long appId = (Long) message.get("appId");
        String deployUrl = (String) message.get("deployUrl");

        log.info("收到截图任务: appId={}, url={}", appId, deployUrl);

        try {
            String screenshotUrl = screenshotService.generateAndUploadScreenshot(deployUrl);
            appService.updateAppCover(appId, screenshotUrl);
        } catch (Exception e) {
            log.error("截图任务失败 appId={} url={}", appId, deployUrl, e);
            // TODO: 这里可以做重试机制，比如把失败任务丢到死信队列
        }
    }
}
