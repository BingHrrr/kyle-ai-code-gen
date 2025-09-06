package com.kyle.kyleaicodegen.model.entitiy;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 截图任务死信队列记录表 实体类。
 *
 * @author Haoran Wang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("screenshot_task_dlq")
public class ScreenshotTaskDlq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * 消息唯一ID
     */
    @Column("messageId")
    private String messageId;

    /**
     * 应用ID
     */
    @Column("appId")
    private Long appId;

    /**
     * 部署URL
     */
    @Column("deployUrl")
    private String deployUrl;

    /**
     * 错误信息
     */
    @Column("errorMessage")
    private String errorMessage;

    /**
     * 重试次数
     */
    @Column("retryCount")
    private Integer retryCount;

    /**
     * 状态：0=未处理，1=已处理成功，2=处理中，3=处理失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    @Column("createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column("updateTime")
    private LocalDateTime updateTime;

}
