package com.kyle.kyleaicodegen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("body")
@Tag(name = "body参数")
public class BodyController {
   @Operation(summary = "普通body请求")
   @GetMapping("/test")
   public ResponseEntity<String> body(){
       return ResponseEntity.ok("ok");
   }
    @GetMapping("/test1")
    public ResponseEntity<String> body2(){
        return ResponseEntity.ok("ok");
    }
}
