package com.kyle.kyleaigenapp;

import dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {RedisEmbeddingStoreAutoConfiguration.class})
@MapperScan("com.kyle.kyleaigenapp.mapper")
@ComponentScan("com.kyle")
public class KyleAiGenAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(KyleAiGenAppApplication.class, args);
    }
}