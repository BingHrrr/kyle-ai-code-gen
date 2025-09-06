package com.kyle.kyleaicodegen.mq.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenshotMessage {
    private Long appId;
    private String deployUrl;
    private String massageId;
}