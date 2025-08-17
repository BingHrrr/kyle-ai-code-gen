package com.kyle.kyleaicodegen.ai;

import com.kyle.kyleaicodegen.ai.model.HtmlCodeResult;
import com.kyle.kyleaicodegen.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class AiCodeGenServiceTest {

    @Resource
    private AiCodeGenService aiCodeGenService;

    @Test
    void generateHtmlCode() {
        HtmlCodeResult result = aiCodeGenService.generateHtmlCode("做个Kyle Wang的工作记录小工具，不超过50行");
        Assertions.assertNotNull(result);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult multiFileCode = aiCodeGenService.generateMultiFileCode("做个Kyle Wang的留言板，不超过50行");
        Assertions.assertNotNull(multiFileCode);
    }
}