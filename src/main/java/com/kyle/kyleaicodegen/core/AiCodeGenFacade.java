package com.kyle.kyleaicodegen.core;

import com.kyle.kyleaicodegen.ai.AiCodeGenService;
import com.kyle.kyleaicodegen.ai.model.HtmlCodeResult;
import com.kyle.kyleaicodegen.ai.model.MultiFileCodeResult;
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
    private AiCodeGenService aiCodeGenService;

    /**
     * 统一入口：根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (codeGenTypeEnum) {
            case HTML -> generateAndSaveHtmlCode(userMessage);
            case MULTI_FILE -> generateAndSaveMultiFileCode(userMessage);
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 生成 HTML 模式的代码并保存
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    private File generateAndSaveHtmlCode(String userMessage) {
        HtmlCodeResult result = aiCodeGenService.generateHtmlCode(userMessage);
        return CodeFileSaver.saveHtmlCodeResult(result);
    }

    /**
     * 生成多文件模式的代码并保存
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    private File generateAndSaveMultiFileCode(String userMessage) {
        MultiFileCodeResult result = aiCodeGenService.generateMultiFileCode(userMessage);
        return CodeFileSaver.saveMultipartResult(result);
    }


    /**
     * 统一入口：根据类型生成并保存代码 (流式)
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (codeGenTypeEnum) {
            case HTML -> generateAndSaveHtmlCodeStream(userMessage);
            case MULTI_FILE -> generateAndSaveMultiFileCodeStream(userMessage);
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 生成 HTML 模式的代码并保存（流式）
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    private Flux<String> generateAndSaveHtmlCodeStream(String userMessage) {
        Flux<String> result = aiCodeGenService.generateHtmlCodeStream(userMessage);
        StringBuilder stringBuilder = new StringBuilder();
        return result
                .doOnNext(stringBuilder::append)
                .doOnComplete(() -> {
                    try {
                        String string = stringBuilder.toString();
                        HtmlCodeResult htmlCodeResult = CodeParser.parseHtmlCode(string);
                        File saveDir = CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
                        log.info("保存目录：{}", saveDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("保存文件失败: {}", e.getMessage());
                    }
                });
    }


    /**
     * 生成多文件模式的代码并保存（流式）
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    private Flux<String> generateAndSaveMultiFileCodeStream(String userMessage) {
        Flux<String> result = aiCodeGenService.generateMultiFileCodeStream(userMessage);
        StringBuilder stringBuilder = new StringBuilder();
        return result
                .doOnNext(stringBuilder::append)
                .doOnComplete(() -> {
                    try {
                        String string = stringBuilder.toString();
                        MultiFileCodeResult multiFileCodeResult = CodeParser.parseMultiFileCode(string);
                        File saveDir = CodeFileSaver.saveMultipartResult(multiFileCodeResult);
                        log.info("多文件保存目录：{}", saveDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("多文件保存文件失败: {}", e.getMessage());
                    }
                });
    }
}

