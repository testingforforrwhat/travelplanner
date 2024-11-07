package com.test.travelplanner.model;


public record RegisterRequest(
       String username,
       String password,
       UserRole role
) {
}
