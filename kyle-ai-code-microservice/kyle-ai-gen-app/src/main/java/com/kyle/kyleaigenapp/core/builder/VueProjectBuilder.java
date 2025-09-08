package com.kyle.kyleaigenapp.core.builder;

import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @author Haoran Wang
 * @since 2025
 */

@Slf4j
@Component
public class VueProjectBuilder {
    /**
     * 异步 虚拟线程 构建Vue项目
     *
     * @param projectPath 项目路径 ROOT_PATH/vue_project_appId
     */
    public void buildProjectAsync(String projectPath) {
        Thread.ofVirtual().name("vue-builder-"+ System.currentTimeMillis())
                .start(
                () -> {
                    try {
                        buildProject(projectPath);
                    } catch (Exception e) {
                        log.error("异步构建Vue项目失败", e);
                    }
                });
    }

    /**
     * 构建Vue项目
     *
     * @param projectPath 项目路径 ROOT_PATH/vue_project_appId
     * @return 是否构建成功
     */
    public boolean buildProject(String projectPath) {
        File projectDir = new File(projectPath);
        if (!projectDir.exists() || !projectDir.isDirectory()) {
            log.error("项目路径不存在或不是一个目录: {}", projectPath);
            return false;
        }
        // 检查package.json文件是否存在
        File packageJson = new File(projectDir, "package.json");
        if (!packageJson.exists()) {
            log.error("package.json 文件不存在: {}", packageJson.getAbsolutePath());
            return false;
        }
        log.info("开始构建Vue项目: {}", projectPath);
        // 执行npm install
        if (!executeNpmInstall(projectDir)) {
            log.error("npm install 失败，无法继续构建");
            return false;
        }
        // 执行npm run build
        if (!executeNpmBuild(projectDir)) {
            log.error("npm run build 失败，无法继续构建");
            return false;
        }
        // 检查dist目录是否生成
        File distDir = new File(projectDir, "dist");
        if (!distDir.exists()) {
            log.error("构建完成，但dist 目录：{}未生成", distDir.getAbsolutePath());
            return false;
        }
        log.info("Vue项目构建完成，dist 目录：{}已生成", distDir.getAbsolutePath());
        return executeNpmBuild(projectDir);
    }

    /**
     * 执行命令
     *
     * @param workingDir     工作目录
     * @param command        命令字符串
     * @param timeoutSeconds 超时时间（秒）
     * @return 是否执行成功
     */
    private boolean executeCommand(File workingDir, String command, int timeoutSeconds) {
        try {
        // 记录开始执行命令的信息日志
            log.info("在目录 {} 中执行命令: {}", workingDir.getAbsolutePath(), command);
            Process process = RuntimeUtil.exec(
                    null,
                    workingDir,
                    command.split("\\s+") // 命令分割为数组
            );
            // 等待进程完成，设置超时
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                log.error("命令执行超时（{}秒），强制终止进程", timeoutSeconds);
                process.destroyForcibly();
                return false;
            }
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                log.info("命令执行成功: {}", command);
                return true;
            } else {
                log.error("命令执行失败，退出码: {}", exitCode);
                return false;
            }
        } catch (Exception e) {
            log.error("执行命令失败: {}, 错误信息: {}", command, e.getMessage());
            return false;
        }
    }

    /**
     * 检查系统是不是windows
     *
     * @return bool
     */
    private boolean checkIfWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    private String buildCommand(String baseCommand) {
        return checkIfWindows() ? baseCommand + ".cmd" : baseCommand;
    }

    /**
     * 执行 npm install 命令
     */
    private boolean executeNpmInstall(File projectDir) {
        log.info("执行 npm install...");
        String command = String.format("%s install", buildCommand("npm"));
        return executeCommand(projectDir, command, 300); // 5分钟超时
    }

    /**
     * 执行 npm run build 命令
     */
    private boolean executeNpmBuild(File projectDir) {
        log.info("执行 npm run build...");
        String command = String.format("%s run build", buildCommand("npm"));
        return executeCommand(projectDir, command, 180); // 3分钟超时
    }

}
