package com.test.travelplanner.authentication.model;


import com.test.travelplanner.authentication.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Get user profile")
    public UserEntity getUserProfile(
            @AuthenticationPrincipal UserEntity user,
            String username) {
        return userService.getUserProfile(username);
    }

    @PostMapping("/updateUserProfile")
    @Operation(summary = "update eUser Profile")
    public int updateUserProfile(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam String email) {
        return userService.updateUserProfile(user.getUsername(),email);
    }
}
