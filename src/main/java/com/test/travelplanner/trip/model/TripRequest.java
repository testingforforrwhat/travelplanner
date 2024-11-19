package com.test.travelplanner.trip.model;


import java.time.LocalDate;


public record TripRequest(
        long userId,
        long destinationId,
        String title,
        Integer days,
        String startDate,
        String status,
        String notes,
        LocalDate createdAt,
        LocalDate updatedAt
) {
}