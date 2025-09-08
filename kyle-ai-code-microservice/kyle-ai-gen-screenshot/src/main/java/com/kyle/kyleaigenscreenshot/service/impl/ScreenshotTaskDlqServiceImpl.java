package com.kyle.kyleaigenscreenshot.service.impl;

import com.kyle.kyleaigenmodel.model.entitiy.ScreenshotTaskDlq;
import com.kyle.kyleaigenscreenshot.mapper.ScreenshotTaskDlqMapper;
import com.kyle.kyleaigenscreenshot.service.ScreenshotTaskDlqService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 截图任务死信队列记录表 服务层实现。
 *
 * @author Haoran Wang
 */
@Service
public class ScreenshotTaskDlqServiceImpl extends ServiceImpl<ScreenshotTaskDlqMapper, ScreenshotTaskDlq>  implements ScreenshotTaskDlqService {

    @Override
    public boolean existsByMessageId(String messageId) {
        QueryWrapper queryWrapper = this.query().eq(ScreenshotTaskDlq::getMessageId, messageId);
        return this.exists(queryWrapper);
    }
}
