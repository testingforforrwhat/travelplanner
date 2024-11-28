package com.test.travelplanner.model.dto;

public record WeatherDTO(
    String date,  
    String icon,  
    Integer temp  
) {}