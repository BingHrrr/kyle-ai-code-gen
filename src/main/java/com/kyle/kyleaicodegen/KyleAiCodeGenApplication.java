package com.kyle.kyleaicodegen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.kyle.kyleaicodegen.mapper")
@SpringBootApplication
public class KyleAiCodeGenApplication {
	public static void main(String[] args) {
		SpringApplication.run(KyleAiCodeGenApplication.class, args);
	}
}
