package com.kyle.kyleaicodegen;

import dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@MapperScan("com.kyle.kyleaicodegen.mapper")
@SpringBootApplication(exclude = {RedisEmbeddingStoreAutoConfiguration.class})
public class KyleAiCodeGenApplication {
	public static void main(String[] args) {
		SpringApplication.run(KyleAiCodeGenApplication.class, args);
	}
}
