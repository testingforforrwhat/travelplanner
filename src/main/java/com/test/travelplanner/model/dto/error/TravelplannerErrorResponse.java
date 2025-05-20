package com.test.travelplanner.model.dto.error;


public record TravelplannerErrorResponse(String exceptionMessage, String errorFrom, String exceptionDetails) {


    public TravelplannerErrorResponse(Exception e) {
        this("An error occurred", e.getMessage(), null);
    }
}
