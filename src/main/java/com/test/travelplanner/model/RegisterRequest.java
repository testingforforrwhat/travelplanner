package com.test.travelplanner.model;


import com.test.travelplanner.model.entity.UserRole;

public record RegisterRequest(
       String username,
       String password,
       UserRole role,
       String email
) {
}
