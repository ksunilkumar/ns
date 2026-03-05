package com.nsind.user.controller;

import com.nsind.common.dto.ApiResponse;
import com.nsind.user.dto.UserProfileDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User profile and account management")
public class UserController {

    @GetMapping("/{userId}")
    @Operation(summary = "Get user profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> getUserProfile(
            @PathVariable String userId) {
        // Mock response - replace with actual service call
        UserProfileDto user = UserProfileDto.builder()
                .id(userId)
                .email("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .companyName("Acme Corp")
                .subscriptionPlan("PROFESSIONAL")
                .isActive(true)
                .createdAt(System.currentTimeMillis())
                .build();

        return ResponseEntity.ok(ApiResponse.success(user, "User profile retrieved"));
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateUserProfile(
            @PathVariable String userId,
            @RequestBody UserProfileDto profile) {
        // Mock update response
        return ResponseEntity.ok(ApiResponse.success(profile, "User profile updated"));
    }
}

