package com.test.travelplanner.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;  
import org.springframework.web.client.RestTemplate;  
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api/weather")  
public class WeatherController {  

    private final WebClient webClient;

    @Value("${tencent.api-key}")
    private String apiKey;

    public WeatherController() {  
        this.webClient = WebClient.create();  
    }

    // 这是 Spring Boot Controller 用于开启 Server-Sent Events（SSE）流的标准方式。
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)  
    public SseEmitter streamWeather(@RequestParam String city) {
        // 设置SSE超时时间
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);  // 长超时
        
        // 异步执行，避免阻塞主线程  
        new Thread(() -> {  
            try {  
                // 初始发送一次数据  
                fetchAndSendWeather(city, emitter);  
                
                // 每30秒调用一次天气API（模拟实时更新）  
                for (int i = 0; i < 20; i++) {  // 限制20次，避免无限循环  
                    Thread.sleep(30000); // 30秒  
                    fetchAndSendWeather(city, emitter);  
                }  
                
                emitter.complete();  
            } catch (Exception e) {  
                emitter.completeWithError(e);  
            }  
        }).start();  
        
        return emitter;  
    }

    /**
     *
     * Java 常用 HTTP 调用库
     *
     * - HttpURLConnection（JDK自带，繁琐，不推荐）
     * - Apache HttpClient（经典，通用）
     * - OkHttp（现代、简单、功能强，流行）
     * - Spring RestTemplate（Spring生态同步方式，简单好用）
     * - Spring WebClient（Spring 5 推荐，响应式支持，WebFlux必备）
     *
     * > 其中 OkHttp 和 WebClient 用于主流第三方HTTP接口（RESTful或SSE）最方便。
     *
     *
     * @param cityLocation
     * @param emitter
     * @throws Exception
     */
    private void fetchAndSendWeather(String cityLocation, SseEmitter emitter) throws Exception {
        // 这里替换为实际的天气API，下面用的是示例  
        String apiUrl = "https://apis.map.qq.com/ws/weather/v1/?" +
                "key=" + apiKey + "&" +
                "location=" + URLEncoder.encode(cityLocation, StandardCharsets.UTF_8) + "&" +
                "type=future&" +
                "output=json&" +
                "get_md=0";
        
        // 调用第三方天气API  
        String weatherData = webClient.get()  
                .uri(apiUrl)  
                .retrieve()  
                .bodyToMono(String.class)  
                .block();  // 在异步线程中，可以使用阻塞方式  
        log.info(" ==> weatherData: {}", weatherData);

        // 发送数据到客户端  
        emitter.send(SseEmitter.event()  
                .name("weather-update")  
                .data(weatherData, MediaType.APPLICATION_JSON));  
    }  
}  