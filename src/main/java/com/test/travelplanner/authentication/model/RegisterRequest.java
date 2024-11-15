package com.test.travelplanner.authentication.model;


public record RegisterRequest(
       String username,
       String password,
       UserRole role
) {
}
