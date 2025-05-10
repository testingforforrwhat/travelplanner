package com.test.travelplanner.external.model;

import lombok.Data;
import java.util.List;  

@Data  
public class WeatherData {  
    private Integer status;  
    private String message;  
    private String request_id;  
    private Result result;  
}  







