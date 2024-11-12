package com.test.travelplanner.destination.model;

public record AttractionDto(
        Long id,
        String name,
        String description,
        String openingHours,
        Double ticketPrice,
        Long destinationId
) {}

