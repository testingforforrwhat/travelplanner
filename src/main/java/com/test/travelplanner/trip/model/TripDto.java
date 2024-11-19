package com.test.travelplanner.trip.model;


import com.test.travelplanner.authentication.model.UserDto;
import com.test.travelplanner.destination.model.DestinationDto;


public record TripDto(
        Long id,
        String title,
        Integer days,
        String startDate,
        String notes,
        UserDto user,
        DestinationDto destination
) {
    public TripDto(com.test.travelplanner.trip.model.TripEntity entity) {
        this(
                entity.getId(),
                entity.getTitle(),
                entity.getDays(),
                entity.getStartDate(),
                entity.getNotes(),
                new UserDto(entity.getUser()),
                new DestinationDto(entity.getDestination())
        );
    }
}

