package com.kyle.kyleaicodegen.controller;

import com.kyle.kyleaicodegen.common.BaseResponse;
import com.kyle.kyleaicodegen.common.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/test")
    public BaseResponse<String> healthCheck() {
        return ResultUtils.success( "ok");
    }
}

