package com.test.travelplanner.external.model.WeatherData;

import lombok.Data;

@Data
public class Info {  
    private String date;  
    private String week;  
    private DayNightDetail day;  
    private DayNightDetail night;  
}  