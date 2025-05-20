package com.test.travelplanner.config.storage;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  
public class TencentCosConfig {  

    @Value("${tencent.cos.secret-id}")  
    private String secretId;  

    @Value("${tencent.cos.secret-key}")  
    private String secretKey;  

    @Value("${tencent.cos.region}")  
    private String region;  

    @Bean  
    public COSClient cosClient() {  
        // 创建 COS 凭证  
        COSCredentials credentials = new BasicCOSCredentials(secretId, secretKey);  
        // 设置区域  
        ClientConfig clientConfig = new ClientConfig(new Region(region));  
        // 创建 COS 客户端  
        return new COSClient(credentials, clientConfig);  
    }  
}