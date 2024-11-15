//package com.test.travelplanner.model;
//
//
//public record TripDto(
//        Long id,
//        String title,
//        Integer days,
//        String startDate,
//        String notes,
//        UserDto user,
//        CityDto city
//) {
//    public TripDto(TripEntity entity) {
//        this(
//                entity.getId(),
//                entity.getTitle(),
//                entity.getDays(),
//                entity.getStartDate(),
//                entity.getNotes(),
//                new UserDto(entity.getUser()),
//                new CityDto(entity.getCity())
//        );
//    }
//}
//
