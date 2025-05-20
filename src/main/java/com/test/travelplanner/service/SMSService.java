package com.test.travelplanner.service;

/**
 * 短信模块 业务逻辑层 接口
 * */
public interface SMSService {

    /**
     * 发送短信验证码
     * @param phone 接收短信验证码的手机号
     * @return 发送短信验证码是否成功
     * */
    boolean sendValidateSMS( String phone );

}
