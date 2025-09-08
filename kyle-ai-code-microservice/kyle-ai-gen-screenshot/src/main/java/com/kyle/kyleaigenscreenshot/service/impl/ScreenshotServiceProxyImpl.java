package com.kyle.kyleaigenscreenshot.service.impl;

import com.kyle.kyleaigenclient.proxy.ScreenshotServiceProxy;
import com.kyle.kyleaigenscreenshot.service.ScreenshotService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Haoran Wang
 * @since 2025
 */

@DubboService
public class ScreenshotServiceProxyImpl implements ScreenshotServiceProxy {
    @Resource
    private ScreenshotService screenshotService;

    @Override
    public String generateAndUploadScreenshot(String webUrl) {
        return screenshotService.generateAndUploadScreenshot(webUrl);
    }
}
