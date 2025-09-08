package com.kyle.kyleaigenscreenshot.utils;


import com.kyle.kyleaigencommon.exception.BusinessException;
import com.kyle.kyleaigencommon.exception.ErrorCode;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * WebDriver 工厂类
 * 每次调用都会生成一个新的 ChromeDriver 实例，避免多线程共享问题
 *
 * @author Haoran
 */
@Slf4j
public class WebDriverFactory {

    static {
        // 只需全局调用一次，确保 chromedriver 可用
        WebDriverManager.firefoxdriver().setup();
    }

    /**
     * 创建新的 ChromeDriver
     */
    public static WebDriver createDriver() {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");               // 无头模式
            options.addArguments("--disable-gpu");            // 禁用 GPU
            options.addArguments("--no-sandbox");             // Docker 环境必需
            options.addArguments("--disable-dev-shm-usage");  // 避免 /dev/shm 过小导致崩溃
            options.addArguments("--disable-extensions");     // 禁用扩展
            options.addArguments("--window-size=1600,900");   // 固定窗口大小
            options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

            WebDriver driver = new ChromeDriver(options);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return driver;
        } catch (Exception e) {
            log.error("创建 ChromeDriver 失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建 ChromeDriver 失败");
        }
    }
}
