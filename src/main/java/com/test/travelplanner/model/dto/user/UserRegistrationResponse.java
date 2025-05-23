package com.test.travelplanner.model.dto.user;

import com.test.travelplanner.model.entity.UserRole;
import lombok.Data;

@Data  
public class UserRegistrationResponse {
    private String username;
    private String email;  
    private String phone;  
//    private String password;
    private UserRole role;
}