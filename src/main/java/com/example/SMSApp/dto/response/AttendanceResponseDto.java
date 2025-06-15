package com.example.SMSApp.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponseDto {

    private UUID publicId;

    private LocalDateTime date;

    private Boolean present;

    private UUID studentId;
    private String studentName;

    private UUID lessonId;
    private String lessonName;
}
