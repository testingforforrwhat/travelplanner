package com.test.travelplanner.controller;


import com.test.travelplanner.model.dto.unifiedGlobalResponse.ApiResponse;
import com.test.travelplanner.model.dto.user.LoginAuthResponse;
import com.test.travelplanner.model.entity.UserEntity;
import com.test.travelplanner.service.impl.AuthenticationService;
import com.test.travelplanner.model.LoginRequest;
import com.test.travelplanner.model.LoginResponse;
import com.test.travelplanner.model.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {


   private final AuthenticationService authenticationService;


   public AuthenticationController(AuthenticationService authenticationService) {
       this.authenticationService = authenticationService;
   }


   @PostMapping("/register")
   @ResponseStatus(HttpStatus.CREATED)
   @Operation(summary = "register")
   public UserEntity register(@RequestBody RegisterRequest body) {
       return authenticationService.register(
               body.username(),
               body.password(),
               body.role(),
               body.email());
   }


   @PostMapping("/login")
   public ResponseEntity<ApiResponse<LoginAuthResponse>> login(@RequestBody LoginRequest body) {
       LoginAuthResponse authResponse = authenticationService.login(body.username(), body.password());
       return ResponseEntity.status(HttpStatus.OK)
               .body(ApiResponse.success(authResponse));
   }
}
