package com.nsind.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String companyName;
    private String subscriptionPlan;
    private Boolean isActive;
    private Boolean isEmailVerified;
    private Long createdAt;
    private Long updatedAt;
}

