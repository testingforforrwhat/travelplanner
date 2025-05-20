package com.test.travelplanner.controller;


import com.test.travelplanner.model.dto.unifiedGlobalResponse.ApiResponse;
import com.test.travelplanner.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sms")
@CrossOrigin        // 可以在支持跨域的方法或者类添加该注解
public class SMSController {

    // 依赖项
    @Autowired
    private SMSService smsService;

    /**
     * 发送短信验证码
     * http://127.0.0.1:8081/sms/validate/{phone}
     * @param phone 接收短信验证码的手机号
     * @return 响应报文体
     * */
    @GetMapping("/validate/{phone}")
    public ResponseEntity<ApiResponse<Map<String,Object>>> sendValidateSMS(@PathVariable String phone ){
        // Get请求参数：
        // url = http://127.0.0.1:8081/sms/validate?phone=**
        // 伪静态：（URL参数）
        // url = http://127.0.0.1:8081/sms/validate/**

        // 响应报文体
        Map<String,Object> responseBody = new HashMap<>();

        // 实现 发送短信验证码的 业务流程 => 业务逻辑层
        if( smsService.sendValidateSMS( phone ) ){
            // 发送短信 业务成功
            responseBody.put( "code" , 200 );
            responseBody.put( "message" , "短信验证码发送成功" );
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(responseBody));
        }else{
            // 发送短信 业务失败
            responseBody.put( "code" , 500 );
            responseBody.put( "message" , "短信验证码发送失败" );
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.error(responseBody));
        }

    }

}
