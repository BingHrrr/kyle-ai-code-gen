package com.kyle.kyleaicodegen.core.saver;

import cn.hutool.core.util.StrUtil;
import com.kyle.kyleaicodegen.ai.model.HtmlCodeResult;
import com.kyle.kyleaicodegen.exception.BusinessException;
import com.kyle.kyleaicodegen.exception.ErrorCode;
import com.kyle.kyleaicodegen.model.enums.CodeGenTypeEnum;

/**
 * html代码文件保存模板
 * @author Haoran Wang
 * @since 2025
 */

public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult>{


    @Override
    protected void validate(HtmlCodeResult result) {
        super.validate(result);
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "html代码为空");
        }
    }

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult htmlCodeResult, String baseDir) {
        // 文件名
        String filename = "index.html";
        writeToFile(baseDir, filename, htmlCodeResult.getHtmlCode());
    }
}
