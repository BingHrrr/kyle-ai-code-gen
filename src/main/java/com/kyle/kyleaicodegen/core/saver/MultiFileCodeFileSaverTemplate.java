package com.kyle.kyleaicodegen.core.saver;

import cn.hutool.core.util.StrUtil;
import com.kyle.kyleaicodegen.ai.model.MultiFileCodeResult;
import com.kyle.kyleaicodegen.exception.BusinessException;
import com.kyle.kyleaicodegen.exception.ErrorCode;
import com.kyle.kyleaicodegen.model.enums.CodeGenTypeEnum;

/**
 * 多文件代码文件保存模板
 * @author Haoran Wang
 * @since 2025
 */

public class MultiFileCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult>{

    @Override
    protected void validate(MultiFileCodeResult result) {
        super.validate(result);
        if(StrUtil.isBlank(result.getHtmlCode())){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "html代码为空");
        }
    }

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void saveFiles(MultiFileCodeResult result, String baseDir) {
        writeToFile(baseDir, "index.html", result.getHtmlCode());
        writeToFile(baseDir, "style.css", result.getCssCode());
        writeToFile(baseDir, "script.js", result.getJsCode());
    }


}
