package com.kyle.kyleaicodegen.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.kyle.kyleaicodegen.constant.AppConstant;
import com.kyle.kyleaicodegen.exception.BusinessException;
import com.kyle.kyleaicodegen.exception.ErrorCode;
import com.kyle.kyleaicodegen.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author Haoran Wang
 * @since 2025
 */

public abstract class CodeFileSaverTemplate<T> {
    /**
     * 根目录
     */
    protected static final String ROOT_DIR = AppConstant.CODE_OUTPUT_ROOT_DIR;

    /**
     *
     * @param result
     * @return
     */
    public final File saveCode(T result, Long appId) {
        // 校验参数
        validate(result);
        // 构建唯一目录
        String baseDir = buildUniqueDir(appId);
        // 保存文件（子类实现）
        saveFiles(result, baseDir);
        // 返回文件目录对象
        return new File(baseDir);
    }

    /**
     * 校验参数（子类实现）
     * @param result 保存结果
     */
    protected void validate(T result) {
        if(result == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存结果为空");
        }
    }

    /**
     * 构建一个唯一的目录路径 tmp/code_output/{bizType}_{uniqueId}
     *
     * @return 返回创建好的完整目录路径
     */
    protected final String buildUniqueDir(Long appId) {
        String bizType = getCodeType().getValue();
        String uniqueDir = StrUtil.format("{}_{}", bizType, appId);
        String dirPath = ROOT_DIR + File.separator + uniqueDir;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    // 保存单个文件
    protected final void writeToFile(String dirPath, String filename, String content) {
        if (StrUtil.isNotBlank(content)) {
            String filePath = dirPath + File.separator + filename;
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
        }
    }

    /**
     * 获取代码生成类型（子类实现）
     * @return 代码生成类型
     */
    protected abstract CodeGenTypeEnum getCodeType();

    /**
     * 根据类型构建唯一目录
     * @param baseDir 根目录
     * @param result 保存的内容
     */
    protected abstract void saveFiles(T result,String baseDir);
}
