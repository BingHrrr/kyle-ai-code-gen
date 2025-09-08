package com.kyle.kyleaigenuser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


//@EnableDubbo
@SpringBootApplication
@MapperScan("com.kyle.kyleaigenuser.mapper")
@ComponentScan("com.kyle")
public class KyleAiGenUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(KyleAiGenUserApplication.class, args);
    }
}
