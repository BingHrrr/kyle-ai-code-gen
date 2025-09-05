package com.kyle.kyleaicodegen.ai.tools;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Haoran Wang
 * @since 2025
 */

@Slf4j
@Component
public class ToolManager {
    private final Map<String, BaseTool> toolMap = new HashMap<>();

    /**
     * 工具列表
     */
    @Resource
    private BaseTool[] tools;

    /**
     * 初始化工具映射
     */
    @PostConstruct
    public void initTools() {
        for(BaseTool tool : tools) {
            toolMap.put(tool.getToolName(), tool);
            log.info("注册工具: {} -> {}", tool.getToolName(), tool.getDisplayName());
        }
        log.info("工具注册完成，共注册了 {} 个工具", toolMap.size());
    }

    /**
     * 根据工具名称获取工具实例
     * @param name 工具名称
     * @return 工具实例
     */
    public BaseTool getTool(String name) {
        return toolMap.get(name);
    }

    /**
     * 获取所有工具
     * @return 工具列表
     */
    public BaseTool[] getAllTools() {
        return tools;
    }
}
