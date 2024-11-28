package com.test.travelplanner.model.dto;

import java.util.List;

public record DestinationDTO(
    String id,  
    String name,  
    String climate,  
    String bestTime,  
    String timezone,  
    String description,  
    List<ImageDTO> images,
    List<AttractionDTO> attractions,  
    PracticalInfoDTO practicalInfo,  
    List<WeatherDTO> weather,  
    List<RecommendationDTO> recommendations  
) {}