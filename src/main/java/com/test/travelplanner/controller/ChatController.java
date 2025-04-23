package com.test.travelplanner.controller;


import com.test.travelplanner.service.DeepSeekService;
import org.springframework.web.bind.annotation.*;

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
}  