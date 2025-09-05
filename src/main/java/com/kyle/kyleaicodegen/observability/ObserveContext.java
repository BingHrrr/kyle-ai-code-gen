package com.kyle.kyleaicodegen.observability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 监控上下文消息类
 *
 * @author Haoran Wang
 * @since 2025
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObserveContext implements Serializable {
    // 应用ID
    private String appId;
    // 用户ID
    private String userId;

    @Serial
    private static final long serialVersionUID = 1L;
}
