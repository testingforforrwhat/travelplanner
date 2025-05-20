package com.test.travelplanner.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *
 * https://api.aliyun.com/api-tools/sdk/Dysmsapi?version=2017-05-25&language=java-async-tea&tab=primer-doc
 * https://help.aliyun.com/zh/sms/getting-started/use-sms-api?spm=5176.25163407.console-base_help.dexternal.774d2ec8cWrUpp
 * 
 */
@Configuration
public class AliSMSClient {

    @Value("${sms.accessKeyId}")
    private String accessKeyId;
    @Value("${sms.accessKeySecret}")
    private String accessKeySecret;
    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception
     */
    @Bean
    public Client createClient() throws Exception {

        Config config = new Config()
                // 您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        Client client = new Client(config);
        return client;

    }

}
