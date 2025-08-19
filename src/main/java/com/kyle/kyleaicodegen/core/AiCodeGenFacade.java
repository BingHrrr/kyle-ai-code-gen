package com.kyle.kyleaicodegen.core;

import com.kyle.kyleaicodegen.ai.AiCodeGenService;
import com.kyle.kyleaicodegen.ai.AiCodeGenServiceFactory;
import com.kyle.kyleaicodegen.ai.model.HtmlCodeResult;
import com.kyle.kyleaicodegen.ai.model.MultiFileCodeResult;
import com.kyle.kyleaicodegen.core.parser.CodeParserExecutor;
import com.kyle.kyleaicodegen.core.saver.CodeFileSaverExecutor;
import com.kyle.kyleaicodegen.exception.BusinessException;
import com.kyle.kyleaicodegen.exception.ErrorCode;
import com.kyle.kyleaicodegen.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * 代码生成门面 组合生成和保存功能 【门面模式】
 *
 * @author Haoran Wang
 * @since 2025
 */
@Slf4j
@Service
public class AiCodeGenFacade {

    @Resource
    private AiCodeGenServiceFactory aiCodeGenServiceFactory;

    /**
     * 统一入口：根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        AiCodeGenService aiCodeGenService = aiCodeGenServiceFactory.getAiCodeGenService(appId);
        return switch (codeGenTypeEnum) {
            // 非流式无需解析
            case HTML -> {
                HtmlCodeResult result = aiCodeGenService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult result = aiCodeGenService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }


    /**
     * 统一入口：根据类型生成并保存代码 (流式)
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        AiCodeGenService aiCodeGenService = aiCodeGenServiceFactory.getAiCodeGenService(appId);
        return switch (codeGenTypeEnum) {
            // 流式输出 需要先解析再保存
            case HTML -> {
                Flux<String> htmlCodeStream = aiCodeGenService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(htmlCodeStream, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                Flux<String> multiFileCodeStream = aiCodeGenService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(multiFileCodeStream, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 通用的流式代码处理方法
     *
     * @param codeStream 代码流
     * @param codeGenTypeEnum 生成类型
     * @return 处理后的代码流
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        StringBuilder stringBuilder = new StringBuilder();
        return codeStream
                .doOnNext(stringBuilder::append)
                .doOnComplete(() -> {
                    try {
                        String content = stringBuilder.toString();
                        // 代码解析
                        Object parseResult = CodeParserExecutor.executeParser(content, codeGenTypeEnum);
                        File saveDir = CodeFileSaverExecutor.executeSaver(parseResult, codeGenTypeEnum, appId);
                        log.info("文件保存目录：{}", saveDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("文件保存文件失败: {}", e.getMessage());
                    }
                });
    }

}

