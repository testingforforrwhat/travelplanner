package com.test.travelplanner.authentication.model;


public record LoginRequest(
       String username,
       String password
) {
}
