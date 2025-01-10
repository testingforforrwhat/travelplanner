package com.test.travelplanner.model.dto;


import com.test.travelplanner.model.entity.TripEntity;


public record TripDto(
        Long id,
        String title,
        Integer days,
        String startDate,
        String notes,
        UserDto user,
        DestinationDto destination
) {
    public TripDto(TripEntity entity) {
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

