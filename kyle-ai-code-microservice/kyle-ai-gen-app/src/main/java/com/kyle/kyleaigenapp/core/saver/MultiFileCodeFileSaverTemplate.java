package com.kyle.kyleaigenapp.core.saver;

import cn.hutool.core.util.StrUtil;
import com.kyle.kyleaigenai.ai.model.MultiFileCodeResult;
import com.kyle.kyleaigencommon.exception.BusinessException;
import com.kyle.kyleaigencommon.exception.ErrorCode;
import com.kyle.kyleaigenmodel.model.enums.CodeGenTypeEnum;


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
