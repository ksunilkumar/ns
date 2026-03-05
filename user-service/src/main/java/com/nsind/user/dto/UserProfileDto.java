package com.nsind.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String companyName;
    private String subscriptionPlan;
    private Boolean isActive;
    private Long createdAt;
}

