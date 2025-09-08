package com.kyle.kyleaigenscreenshot.controller;


import com.kyle.kyleaigenmodel.model.entitiy.ScreenshotTaskDlq;
import com.kyle.kyleaigenscreenshot.service.ScreenshotTaskDlqService;
import com.mybatisflex.core.paginate.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 截图任务死信队列记录表 控制层。
 *
 * @author Haoran Wang
 */
@RestController
@RequestMapping("/screenshotTaskDlq")
public class ScreenshotTaskDlqController {

    @Autowired
    private ScreenshotTaskDlqService screenshotTaskDlqService;

    /**
     * 保存截图任务死信队列记录表。
     *
     * @param screenshotTaskDlq 截图任务死信队列记录表
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ScreenshotTaskDlq screenshotTaskDlq) {
        return screenshotTaskDlqService.save(screenshotTaskDlq);
    }

    /**
     * 根据主键删除截图任务死信队列记录表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return screenshotTaskDlqService.removeById(id);
    }

    /**
     * 根据主键更新截图任务死信队列记录表。
     *
     * @param screenshotTaskDlq 截图任务死信队列记录表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ScreenshotTaskDlq screenshotTaskDlq) {
        return screenshotTaskDlqService.updateById(screenshotTaskDlq);
    }

    /**
     * 查询所有截图任务死信队列记录表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ScreenshotTaskDlq> list() {
        return screenshotTaskDlqService.list();
    }

    /**
     * 根据主键获取截图任务死信队列记录表。
     *
     * @param id 截图任务死信队列记录表主键
     * @return 截图任务死信队列记录表详情
     */
    @GetMapping("getInfo/{id}")
    public ScreenshotTaskDlq getInfo(@PathVariable Long id) {
        return screenshotTaskDlqService.getById(id);
    }

    /**
     * 分页查询截图任务死信队列记录表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ScreenshotTaskDlq> page(Page<ScreenshotTaskDlq> page) {
        return screenshotTaskDlqService.page(page);
    }

}
