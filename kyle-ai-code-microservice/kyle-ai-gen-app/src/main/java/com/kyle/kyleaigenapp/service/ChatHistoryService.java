package com.kyle.kyleaigenapp.service;


import com.kyle.kyleaigenmodel.model.dto.chathistory.ChatHistoryQueryRequest;
import com.kyle.kyleaigenmodel.model.entitiy.ChatHistory;
import com.kyle.kyleaigenmodel.model.entitiy.User;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author Haoran Wang
 */
public interface ChatHistoryService extends IService<ChatHistory> {
    /**
     * 添加对话记录到数据库Mysql
     * @param appId 应用ID
     * @param message 消息内容
     * @param messageType 消息类型 user/ai
     * @param userId 用户ID
     * @return
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * 根据应用ID删除对话记录
     * @param appId 应用ID
     * @return 结果
     */
    boolean deleteByAppId(Long appId);

    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);

    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);
}
