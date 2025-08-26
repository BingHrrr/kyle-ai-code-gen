package com.kyle.kyleaicodegen.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Haoran Wang
 * @since 2025
 */

public interface ProjectDownloadService {
    void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse response);
}
