package com.test.travelplanner.service;

import dev.langchain4j.model.chat.ChatLanguageModel;  
import dev.langchain4j.model.openai.OpenAiChatModel;  
import dev.langchain4j.service.SystemMessage;  
import org.springframework.beans.factory.annotation.Value;  
import org.springframework.stereotype.Service;  

@Service  
public class DeepSeekService {  

    private final ChatLanguageModel chatModel;

    /**
     *
     * 通过构造函数注入配置参数，然后基于（类似 OpenAI SDK 的）OpenAiChatModel / ChatLanguageModel
     * 构建 DeepSeekService 类AI服务的封装 Service，例如用于与 DeepSeek 的接口通信。
     *
     *
     * 如果你的配置较多，更符合 Spring 推荐风格的做法是用 @ConfigurationProperties 批量注入：
     *
     * ```java
     * @Component
     * @ConfigurationProperties(prefix = "deepseek")
     * @Data
     * public class DeepSeekProperties {
     *     private String apiKey;
     *     private String baseUrl;
     *     private String model;
     * }
     * ```
     * 
     * 然后在 Service 里@Autowired 注入 DeepSeekProperties，更加优雅、可维护。
     *
     * @param apiKey
     * @param baseUrl
     * @param model
     */
    public DeepSeekService(  
            @Value("${deepseek.api-key}") String apiKey,  
            @Value("${deepseek.base-url}") String baseUrl,  
            @Value("${deepseek.model}") String model  
    ) {  
        this.chatModel = OpenAiChatModel.builder()  
                .apiKey(apiKey)  
                .baseUrl(baseUrl)  
                .modelName(model)  
                .build();  
    }  

    public String chat(String message) {  
        return chatModel.generate(message);  
    }  
}  