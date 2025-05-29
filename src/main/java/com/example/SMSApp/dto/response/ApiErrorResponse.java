package com.example.SMSApp.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ApiErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path; // Optional: set via HttpServletRequest
    private final List<String> errors; // Optional: for validation errors
}