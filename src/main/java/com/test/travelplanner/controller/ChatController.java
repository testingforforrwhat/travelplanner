package com.test.travelplanner.controller;


import com.test.travelplanner.service.DeepSeekService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.output.Response;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;  

@RestController  
@RequestMapping("/api/ai")  
@CrossOrigin("*") // 允许前端跨域访问  
public class ChatController {  

    private final DeepSeekService deepSeekService;

    public ChatController(DeepSeekService deepSeekService) {  
        this.deepSeekService = deepSeekService;  
    }  

    @PostMapping("/chat")  
    public Map<String, String> chat(@RequestBody Map<String, String> request) {  
        String message = request.get("message");  
        String response = deepSeekService.chat(message);  
        
        Map<String, String> result = new HashMap<>();  
        result.put("response", response);  
        return result;  
    }

    /**
     *
     * https://docs.langchain4j.dev/tutorials/ai-services#streaming - The AI Service can stream response token-by-token
     * https://docs.langchain4j.dev/tutorials/response-streaming/ - LLMs generate text one token at a time, so many LLM providers offer a way to stream the response token-by-token instead of waiting for the entire text to be generated. This significantly improves the user experience, as the user does not need to wait an unknown amount of time and can start reading the response almost immediately.
     * https://docs.langchain4j.dev/tutorials/spring-boot-integration/ - Spring Boot starters help with creating and configuring language models, embedding models, embedding stores, and other core LangChain4j components through properties.
     *
     * @param request
     * @return
     */
    @PostMapping("/chat/sse")
    public SseEmitter chatSse(@RequestBody Map<String, String> request) {
        SseEmitter emitter = new SseEmitter(0L); // 无超时
        String message = request.get("message");

        // 设置完成和错误回调
        emitter.onCompletion(() -> System.out.println("SSE completed"));
        emitter.onError(ex -> System.err.println("SSE error: " + ex.getMessage()));

        // 启动异步处理
        new Thread(() -> {
            try {
                deepSeekService.chatStream(message, new StreamingResponseHandler<AiMessage>() {
                    @Override
                    public void onNext(String token) {
                        try {
                            emitter.send(SseEmitter.event().data(token));
                        } catch (IOException e) {
                            // 处理发送错误
                            onError(e);
                        }
                    }

                    @Override
                    public void onComplete(Response<AiMessage> response) {
                        try {
                            // 可选：发送完成标记
                            emitter.send(SseEmitter.event().name("complete").data(""));
                            emitter.complete();
                        } catch (IOException e) {
                            onError(e);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        emitter.completeWithError(error);
                    }
                });
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        }).start();

        return emitter;
    }

}  