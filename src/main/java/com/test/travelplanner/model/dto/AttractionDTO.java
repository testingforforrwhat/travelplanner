package com.test.travelplanner.model.dto;

public record AttractionDTO(
    String id,  
    String name,  
    String image,  
    String description,  
    String duration,  
    String address  
) {}