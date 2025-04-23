package com.test.travelplanner.model.dto;


import com.test.travelplanner.model.entity.UserEntity;
import com.test.travelplanner.model.entity.UserRole;

public record UserDto(
       Long id,
       String username,
       UserRole role
) {
   public UserDto(UserEntity entity) {
       this(
               entity.getId(),
               entity.getUsername(),
               entity.getRole()
       );
   }
}
