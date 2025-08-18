package com.kyle.kyleaicodegen.service;

import com.kyle.kyleaicodegen.model.dto.app.AppQueryRequest;
import com.kyle.kyleaicodegen.model.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.kyle.kyleaicodegen.model.entitiy.App;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author Haoran Wang
 */
public interface AppService extends IService<App> {

    AppVO getAppVO(App app);

    List<AppVO> getAppVOList(List<App> appList);

    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);
}
