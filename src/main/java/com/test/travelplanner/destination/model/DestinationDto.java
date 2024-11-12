package com.test.travelplanner.destination.model;

import java.util.List;

public record DestinationDto(
        Long id,
        String name,
        String location,
        String description,
        String imageUrl,
        Double averageRating,
        List<AttractionDto> attractions
) {}

