package com.kyle.kyleaicodegen.service;

import com.kyle.kyleaicodegen.model.dto.app.AppAddRequest;
import com.kyle.kyleaicodegen.model.dto.app.AppQueryRequest;
import com.kyle.kyleaicodegen.model.entitiy.User;
import com.kyle.kyleaicodegen.model.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.kyle.kyleaicodegen.model.entitiy.App;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author Haoran Wang
 */
public interface AppService extends IService<App> {

    void generateAppScreenshotAsync(Long appId, String appUrl);

    /**
     * 通过对话生成应用代码
     *
     * @param appId 应用 ID
     * @param message 提示词
     * @param loginUser 登录用户
     * @return
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);


    /**
     * 获取应用封装类
     *
     * @param app
     * @return
     */
    AppVO getAppVO(App app);

    /**
     * 获取应用封装类列表
     *
     * @param appList
     * @return
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 构造应用查询条件
     *
     * @param appQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    Long createApp(AppAddRequest appAddRequest, User loginUser);

    /**
     *
     * @param appId 应用 id
     * @param loginUser 登录用户
     * @return 部署url
     */
    String deployApp(Long appId, User loginUser);
}
