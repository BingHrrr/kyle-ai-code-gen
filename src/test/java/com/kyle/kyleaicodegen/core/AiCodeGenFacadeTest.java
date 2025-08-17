package com.kyle.kyleaicodegen.core;

import com.kyle.kyleaicodegen.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

@SpringBootTest
class AiCodeGenFacadeTest {

    @Resource
    private AiCodeGenFacade aiCodeGenFacade;

    @Test
    void generateAndSaveCode() {
        File file = aiCodeGenFacade.generateAndSaveCode("生成一个登录界面，不超过50行代码", CodeGenTypeEnum.HTML);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateAndSaveCodeStream() {
        Flux<String> codeStream = aiCodeGenFacade.generateAndSaveCodeStream("登陆页面，不超过50行", CodeGenTypeEnum.MULTI_FILE);
        // 阻塞等待所有数据收集完成
        List<String> result = codeStream.collectList().block();
        // 验证结果
        Assertions.assertNotNull(result);
        String completeContent = String.join("", result);
        Assertions.assertNotNull(completeContent);
    }

}
