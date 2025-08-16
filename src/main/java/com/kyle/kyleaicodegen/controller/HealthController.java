package com.kyle.kyleaicodegen.controller;

import com.kyle.kyleaicodegen.common.BaseResponse;
import com.kyle.kyleaicodegen.common.ResultUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/test")
    public BaseResponse<String> healthCheck() {
        return ResultUtils.success( "ok");
    }
}

