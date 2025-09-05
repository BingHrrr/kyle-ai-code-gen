package com.kyle.kyleaicodegen.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.kyle.kyleaicodegen.model.entitiy.ScreenshotTaskDlq;
import com.kyle.kyleaicodegen.mapper.ScreenshotTaskDlqMapper;
import com.kyle.kyleaicodegen.service.ScreenshotTaskDlqService;
import org.springframework.stereotype.Service;

/**
 * 截图任务死信队列记录表 服务层实现。
 *
 * @author Haoran Wang
 */
@Service
public class ScreenshotTaskDlqServiceImpl extends ServiceImpl<ScreenshotTaskDlqMapper, ScreenshotTaskDlq>  implements ScreenshotTaskDlqService{

}
