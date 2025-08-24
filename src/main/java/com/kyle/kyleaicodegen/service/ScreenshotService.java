package com.kyle.kyleaicodegen.service;

/**
 * @author Haoran Wang
 * @since 2025
 */

public interface ScreenshotService {

    /**
     * 生成并上传截图的方法
     * @param webUrl 要生成截图的网页URL地址
     * @return 上传成功的URL
     */
    public String generateAndUploadScreenshot(String webUrl);
}
