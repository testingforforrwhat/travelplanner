package com.test.travelplanner.model.dto;

import com.test.travelplanner.model.entity.AttractionEntity;

public record AttractionDto(
        Long id,
        String name,
        String description,
        String openingHours,
        Double ticketPrice,
        String imageUrl,
        Long destinationId
) {
    public AttractionDto(AttractionEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getOpeningHours(),
                entity.getTicketPrice(),
                entity.getImageUrl(),
                entity.getDestination().getId()
        );
    }
}

