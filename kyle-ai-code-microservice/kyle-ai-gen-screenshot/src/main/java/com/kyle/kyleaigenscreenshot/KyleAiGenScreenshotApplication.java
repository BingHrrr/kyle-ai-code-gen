package com.kyle.kyleaigenscreenshot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.kyle.kyleaigenscreenshot.mapper")
@ComponentScan("com.kyle")
public class KyleAiGenScreenshotApplication {
    public static void main(String[] args) {
        SpringApplication.run(KyleAiGenScreenshotApplication.class, args);
    }
}
