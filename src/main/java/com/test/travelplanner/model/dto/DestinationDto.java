package com.test.travelplanner.model.dto;

import com.test.travelplanner.model.entity.DestinationEntity;

import java.util.List;

public record DestinationDto(
        Long id,
        String name,
        String location,
        String description,
        String imageUrl,
        Double averageRating,
        List<AttractionDto> attractions
) {
    public DestinationDto(DestinationEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getDescription(),
                entity.getImageUrl(),
                entity.getAverageRating(),
                entity.getAttractions().stream().map(AttractionDto::new).toList()
        );
    }
}

