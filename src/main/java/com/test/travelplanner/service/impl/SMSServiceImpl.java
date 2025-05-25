package com.test.travelplanner.service.impl;


import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.test.travelplanner.config.AliSMSClient;
import com.test.travelplanner.redis.RedisUtil;
import com.test.travelplanner.service.SMSService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 短信模块 业务逻辑层 实现类
 * */
@Service
@Slf4j
public class SMSServiceImpl implements SMSService {

    // 依赖项
    @Resource
    private AliSMSClient aliSMSClient;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 发送短信验证码
     *
     * 同时启用断路器和限流，并设置降级方法
     *
     * @param phone 接收短信验证码的手机号
     * @return 发送短信验证码是否成功
     * */
    @CircuitBreaker(name = "backendService", fallbackMethod = "fallbackForCircuitBreaker")
    @RateLimiter(name = "backendService", fallbackMethod = "fallbackForRateLimit")
    @Retry(name = "backendService")
    @Override
    public boolean sendValidateSMS(String phone) {
        // 生成 六位数的随机验证码 [ 100000 , 999999 ]
        int validateNumber = (int)( Math.random() * ( 999999 + 1 - 100000 ) + 100000 );
        // 创建 发送短信的请求对象
        SendSmsRequest request = new SendSmsRequest();
        // 设置 接收短信的手机号码
        request.setPhoneNumbers( phone );
        // 设置 短信签名（企业申请）
        request.setSignName("阿里云短信测试");
        // 设置 短信模板（企业申请）
        request.setTemplateCode("SMS_154950909");
        // 设置 短信模板中的变量参数
        request.setTemplateParam("{\"code\":\"" + validateNumber + "\"}");
        try {
            // 发送 短信请求 得到 短信响应
            SendSmsResponse response = aliSMSClient.createClient().sendSmsWithOptions( request , new RuntimeOptions() );
            // 判断 阿里云短信平台下游业务 发送短信是否成功
            if( "OK".equals( response.getBody().getCode() ) ){
                // 短信验证码发送成功  =>  把正确验证码 持久化存储 Redis中
                redisUtil.set( "SMS-Validate-" + phone , validateNumber , 60 * 30 );
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 断路器降级方法
    public String fallbackForCircuitBreaker(String data, Exception ex) {
        log.warn("Circuit breaker fallback for data: {}, reason: {}", data, ex.getMessage());
        return "Fallback result for circuit breaker: " + data;
    }

    // 限流降级方法
    public String fallbackForRateLimit(String data, Exception ex) {
        log.warn("Rate limiter fallback for data: {}, reason: {}", data, ex.getMessage());
        return "Rate limit exceeded, please try again later: " + data;
    }

}
