package com.kyle.kyleaigenapp.core.saver;
import com.kyle.kyleaigenai.ai.model.HtmlCodeResult;
import com.kyle.kyleaigenai.ai.model.MultiFileCodeResult;
import com.kyle.kyleaigencommon.exception.BusinessException;
import com.kyle.kyleaigencommon.exception.ErrorCode;
import com.kyle.kyleaigenmodel.model.enums.CodeGenTypeEnum;

import java.io.File;


/**
 * 代码文件保存执行器
 * 根据代码生成类型执行相应的保存逻辑
 * @author Haoran Wang
 * @since 2025
 */
public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaver = new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaver = new MultiFileCodeFileSaverTemplate();

    /**
     * 执行代码保存
     *
     * @param codeResult  代码结果对象
     * @param codeGenType 代码生成类型
     * @return 保存的目录
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenType, Long appId) {
        return switch (codeGenType) {
            case HTML -> htmlCodeFileSaver.saveCode((HtmlCodeResult) codeResult, appId);
            case MULTI_FILE -> multiFileCodeFileSaver.saveCode((MultiFileCodeResult) codeResult, appId);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型: " + codeGenType);
        };
    }
}
