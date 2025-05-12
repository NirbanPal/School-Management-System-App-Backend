package com.example.SMSApp.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpSessionDto implements Serializable {
    private String hashedOtp;
    private boolean isValid;
    private LocalDateTime createdAt;
}

