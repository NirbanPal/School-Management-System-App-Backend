package com.example.SMSApp.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponseDto {

    private UUID publicId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private UUID teacherId;
    private String teacherName;

    private UUID classId;
    private String className;
}
