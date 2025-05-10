package com.test.travelplanner.external.model.WeatherData;

import lombok.Data;

@Data
public class DayNightDetail {  
    private String weather;  
    private Integer temperature;  
    private String wind_direction;  
    private String wind_power;  
    private Integer humidity;  
}  