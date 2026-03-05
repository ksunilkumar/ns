package com.nsind.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Standard API Error Response
 * Used across all microservices for consistent error handling
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String code;
    private String message;
    private String details;
    private LocalDateTime timestamp;
    private String path;
    private String traceId;
    private int status;
}

