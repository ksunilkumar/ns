package com.nsind.common.validation;

import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;

/**
 * Validation constants and patterns
 */
@Component
public class ValidationConstants {

    // Email validation pattern
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    // Phone number pattern (international)
    public static final String PHONE_PATTERN = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$";

    // API Key pattern (alphanumeric + hyphens)
    public static final String API_KEY_PATTERN = "^[a-zA-Z0-9\\-]{32,64}$";

    // UUID pattern
    public static final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    // File name pattern (no special characters)
    public static final String FILE_NAME_PATTERN = "^[\\w\\s.-]{1,255}$";

    // Size constants
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 128;
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MIN_CHATBOT_NAME_LENGTH = 3;
    public static final int MAX_CHATBOT_NAME_LENGTH = 255;
    public static final int MAX_DESCRIPTION_LENGTH = 1000;
    public static final int MAX_MESSAGE_LENGTH = 4096;
    public static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10MB

    // Allowed file extensions
    public static final String[] ALLOWED_FILE_EXTENSIONS = {
        ".pdf", ".txt", ".docx", ".doc", ".xlsx", ".xls", ".csv"
    };
}

