package com.kyle.kyleaicodegen.service;

import com.mybatisflex.core.service.IService;
import com.kyle.kyleaicodegen.model.entitiy.ScreenshotTaskDlq;

/**
 * 截图任务死信队列记录表 服务层。
 *
 * @author Haoran Wang
 */
public interface ScreenshotTaskDlqService extends IService<ScreenshotTaskDlq> {
    boolean existsByMessageId(String messageId);
}
