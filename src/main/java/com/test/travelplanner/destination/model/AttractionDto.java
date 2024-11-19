package com.test.travelplanner.destination.model;

public record AttractionDto(
        Long id,
        String name,
        String description,
        String openingHours,
        Double ticketPrice,
        Long destinationId
) {
    public AttractionDto(AttractionEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getOpeningHours(),
                entity.getTicketPrice(),
                entity.getDestination().getId()
        );
    }
}

