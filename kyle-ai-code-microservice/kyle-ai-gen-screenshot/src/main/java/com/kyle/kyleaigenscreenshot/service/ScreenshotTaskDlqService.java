package com.kyle.kyleaigenscreenshot.service;

import com.kyle.kyleaigenmodel.model.entitiy.ScreenshotTaskDlq;
import com.mybatisflex.core.service.IService;

/**
 * 截图任务死信队列记录表 服务层。
 *
 * @author Haoran Wang
 */
public interface ScreenshotTaskDlqService extends IService<ScreenshotTaskDlq> {
    boolean existsByMessageId(String messageId);
}
