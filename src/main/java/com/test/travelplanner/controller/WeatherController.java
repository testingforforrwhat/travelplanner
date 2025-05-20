package com.test.travelplanner.controller;

import com.test.travelplanner.service.impl.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/weather")  
public class WeatherController {

    private final WeatherService weatherService;
    private final WebClient webClient;

    @Value("${tencent.api-key}")
    private String apiKey;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
        this.webClient = WebClient.create();  
    }


    /**
     *
     *
     * 这是 Spring Boot Controller 用于开启 Server-Sent Events（SSE）流的标准方式。
     *
     *
     * SSE：
     *
     * 服务器主动推送：减少无效请求
     * 更低的延迟：数据更新可立即推送给客户端
     * 自动重连：浏览器原生支持断线重连
     * 协议简单：相对于WebSocket更轻量
     * HTTP兼容：可通过现有HTTP基础设施工作
     *
     *
     * @param city
     * @return
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)  
    public SseEmitter streamWeather(@RequestParam String city) {
        // 设置SSE超时时间
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);  // 长超时
        
        // 异步执行，避免阻塞主线程  
        new Thread(() -> {  
            try {  
                // TODO: city to get cityLocation
                // 城市编码查询
                String adcode = weatherService.getCityCode(city);
                if (adcode == null) {
                    adcode = "110000"; // 默认北京
                }

                // 初始发送一次数据
                fetchAndSendWeather(adcode, emitter);
                
                // 每30秒调用一次天气API（模拟实时更新）  
                for (int i = 0; i < 20; i++) {  // 限制20次，避免无限循环  
                    Thread.sleep(30000); // 30秒  
                    fetchAndSendWeather(adcode, emitter);
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
                "adcode=" + cityLocation + "&" +
//                "location=" + URLEncoder.encode(cityLocation, StandardCharsets.UTF_8) + "&" +
                "type=future&" +
                "output=json&" +
                "get_md=0";

        /**
         *
         * 调用第三方天气API, // 获取天气数据
         *
         * 通过WebClient调用了HTTP接口，接口返回的数据直接用String.class接收。
         * weatherData 的数据类型就是 String，内容是一整段 JSON 或纯文本。
         * 你只是得到了原始响应的字符串文本，并没有映射为Java对象。
         *
         * 通过WebClient调用了HTTP接口，接口返回的数据直接用new ParameterizedTypeReference<Map<String,Object>>()接收。
         * weatherData 的数据类型就是 new ParameterizedTypeReference<Map<String,Object>>()，内容是 object。
         * 你得到响应映射为Java对象。
         *
         *
         */
//        Map<String, Object> weatherData = webClient.get()
//                .uri(apiUrl)
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
//                .block();  // 在异步线程中，可以使用阻塞方式
        String weatherData = webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // 在异步线程中，可以使用阻塞方式
        log.info(" ==> weatherData: {}", weatherData);

        assert weatherData != null;
        // 转换为前端需要的格式
        Map<String, Object> convertToClientFormatWeatherData = weatherService.convertToClientFormat(weatherData);
        log.info(" ==> convertToClientFormatWeatherData: {}", convertToClientFormatWeatherData);

        // 发送数据到客户端  
        emitter.send(SseEmitter.event()  
                .name("weather-update")  
                .data(convertToClientFormatWeatherData, MediaType.APPLICATION_JSON));
    }  
}  