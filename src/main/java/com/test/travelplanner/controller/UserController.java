package com.test.travelplanner.controller;


import com.test.travelplanner.model.entity.UserEntity;
import com.test.travelplanner.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Get user profile", security = @SecurityRequirement(name = "bearerAuth"))
    public UserEntity getUserProfile(
            @AuthenticationPrincipal UserEntity currentUser) {
        return userService.getUserProfile(currentUser.getId());
    }

    @PostMapping("/updateUserProfile")
    @Operation(summary = "update User Profile", security = @SecurityRequirement(name = "bearerAuth"))
    public int updateUserProfile(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam String email) {
        return userService.updateUserProfile(user.getUsername(),email);
    }
}
