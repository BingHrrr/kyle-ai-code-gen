package com.kyle.kyleaicodegen.utils;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.kyle.kyleaicodegen.exception.BusinessException;
import com.kyle.kyleaicodegen.exception.ErrorCode;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;


/**
 * @author Haoran Wang
 * @since 2025
 */

@Slf4j
public class PlaywrightScreenshot {

    private static final Playwright playwright = Playwright.create();
    private static final Browser browser;

    //    Playwright playwright = Playwright.create()
    static {
        final int DEFAULT_WIDTH = 1600;
        final int DEFAULT_HEIGHT = 900;
        browser = initChromeDriver(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @PreDestroy
    public void destroy() {
        browser.close();
    }

    /**
     * 相当于打开一个浏览器 无头
     * 初始化 Chrome 浏览器驱动
     */
    private static Browser initChromeDriver(int width, int height) {
        try {
            return playwright.chromium().launch();
        } catch (Exception e) {
            log.error("初始化 Chrome 浏览器失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "初始化 Chrome 浏览器失败");
        }
    }


    /**
     * 压缩图片
     */
    private static void compressImage(String originalImagePath, String compressedImagePath) {
        // 压缩图片质量（0.1 = 10% 质量）
        final float COMPRESSION_QUALITY = 0.3f;
        try {
            ImgUtil.compress(
                    FileUtil.file(originalImagePath),
                    FileUtil.file(compressedImagePath),
                    COMPRESSION_QUALITY
            );
        } catch (Exception e) {
            log.error("压缩图片失败: {} -> {}", originalImagePath, compressedImagePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "压缩图片失败");
        }
    }


    /**
     * 生成网页截图
     *
     * @param webUrl 网页URL
     * @return 压缩后的截图文件路径，失败返回null
     */
    public static String saveWebPageScreenshot(String webUrl) {
        if (StrUtil.isBlank(webUrl)) {
            log.error("网页URL不能为空");
            return null;
        }
        try {
            // 创建临时目录
            String rootPath = System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "screenshots"
                    + File.separator + UUID.randomUUID().toString().substring(0, 8);
            FileUtil.mkdir(rootPath);
            // 图片后缀
            final String IMAGE_SUFFIX = ".png";
            // 原始截图文件路径
            String imageSavePath = rootPath + File.separator + RandomUtil.randomNumbers(5) + IMAGE_SUFFIX;
            // 访问网页
            Page page = browser.newPage();
            // 访问目标网站
//                page.navigate("http://39.96.146.140/dist/y9obGW/");
            page.navigate(webUrl);
            // 等待页面加载完成
            page.waitForLoadState(LoadState.NETWORKIDLE);
            // 截图保存
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(imageSavePath))
                    .setClip(0, 0, 1600, 900)
                    .setFullPage(false));
            log.info("原始截图保存成功: {}", imageSavePath);
            // 压缩图片
            final String COMPRESSION_SUFFIX = "_compressed.jpg";
            String compressedImagePath = rootPath + File.separator + RandomUtil.randomNumbers(5) + COMPRESSION_SUFFIX;
            compressImage(imageSavePath, compressedImagePath);
            log.info("压缩图片保存成功: {}", compressedImagePath);
            // 删除原始图片，只保留压缩图片
            FileUtil.del(imageSavePath);
            return compressedImagePath;
        } catch (Exception e) {
            log.error("网页截图失败: {}", webUrl, e);
            return null;
        }
    }

}

