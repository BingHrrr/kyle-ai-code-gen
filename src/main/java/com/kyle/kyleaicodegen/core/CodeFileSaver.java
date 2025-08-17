package com.kyle.kyleaicodegen.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.kyle.kyleaicodegen.ai.model.HtmlCodeResult;
import com.kyle.kyleaicodegen.ai.model.MultiFileCodeResult;
import com.kyle.kyleaicodegen.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author Haoran Wang
 * @since 2025
 */

public class CodeFileSaver {
    /**
     * 根目录
     */
    private static final String ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 保存html代码
     * @param htmlCodeResult 结构化输出
     * @return 文件
     */
    public static File saveHtmlCodeResult(HtmlCodeResult htmlCodeResult) {
        // 目录
        String baseDir = buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
        // 文件名
        String filename = "index.html";
        writeToFile(baseDir, filename, htmlCodeResult.getHtmlCode());
        return new File(baseDir);
    }

    /**
     * 保存多文件代码
     * @param multiFileCodeResult 结构化输出
     * @return 文件
     */
    public static File saveMultipartResult(MultiFileCodeResult multiFileCodeResult) {
        // 目录
        String baseDir = buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        writeToFile(baseDir, "index.html", multiFileCodeResult.getHtmlCode());
        writeToFile(baseDir, "style.css", multiFileCodeResult.getCssCode());
        writeToFile(baseDir, "script.js", multiFileCodeResult.getJsCode());
        return new File(baseDir);
    }

    /**
     * 构建一个唯一的目录路径 tmp/code_output/{bizType}_{uniqueId}
     *
     * @param bizType 业务类型，用于生成目录名称的一部分
     * @return 返回创建好的完整目录路径
     */
    private static String buildUniqueDir(String bizType) {
        String uniqueDir = StrUtil.format("{}_{}", bizType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = ROOT_DIR + File.separator + uniqueDir;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    // 保存单个文件
    private static void writeToFile(String dirPath, String filename, String content) {
        String filePath = dirPath + File.separator + filename;
        FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
    }
}
