package com.test.travelplanner.config;

import io.swagger.v3.oas.models.OpenAPI;  
import io.swagger.v3.oas.models.info.Info;  
import io.swagger.v3.oas.models.info.Contact;  
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;


// http://localhost:8080/swagger-ui.html
@Configuration  
public class OpenAPIConfig {  
    
    @Bean  
    public OpenAPI customOpenAPI() {  
        return new OpenAPI()  
                .info(new Info()  
                        .title("Travel Planner API")
                        .version("1.0")  
                        .description("API 接口文档")  
                        .contact(new Contact()  
                                .name("travel planner")
                                .email("developer@example.com")));  
    }  
}