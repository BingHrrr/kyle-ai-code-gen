package com.kyle.kyleaigenapp.service.impl;

import com.kyle.kyleaigenapp.service.AppService;
import com.kyle.kyleaigenclient.proxy.AppServiceProxy;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Haoran Wang
 * @since 2025
 */
@DubboService
public class AppServiceProxyImpl implements AppServiceProxy {
    @Resource
    private AppService appService;

    @Override
    public boolean updateAppCover(Long appId, String screenshotUrl) {
        return appService.updateAppCover(appId, screenshotUrl);
    }
}
