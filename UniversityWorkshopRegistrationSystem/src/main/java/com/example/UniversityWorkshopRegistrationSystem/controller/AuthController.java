package com.example.UniversityWorkshopRegistrationSystem.controller;

import com.example.UniversityWorkshopRegistrationSystem.api.ApiResponse;
import com.example.UniversityWorkshopRegistrationSystem.dto.UserRegistrationRequest;
import com.example.UniversityWorkshopRegistrationSystem.dto.UserResponse;
import com.example.UniversityWorkshopRegistrationSystem.mapper.UserMapper;
import com.example.UniversityWorkshopRegistrationSystem.model.User;
import com.example.UniversityWorkshopRegistrationSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public signup endpoint. Login is handled by Spring Security form login.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    public AuthController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserRegistrationRequest request) {
        User created = userService.register(request);
        UserResponse dto = userMapper.toResponse(created);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", dto));
    }
}
