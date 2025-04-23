package com.test.travelplanner.service;

import dev.langchain4j.model.chat.ChatLanguageModel;  
import dev.langchain4j.model.openai.OpenAiChatModel;  
import dev.langchain4j.service.SystemMessage;  
import org.springframework.beans.factory.annotation.Value;  
import org.springframework.stereotype.Service;  

@Service  
public class DeepSeekService {  

    private final ChatLanguageModel chatModel;  

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