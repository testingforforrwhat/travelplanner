package com.test.travelplanner.model;

public record ShareResponse(
        int code,
        String message,
        Object data
){}
