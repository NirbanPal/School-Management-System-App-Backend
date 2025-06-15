package com.example.SMSApp.dto.response;


import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResponseDto {

    private UUID publicId;

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private UUID lessonId;
    private String lessonName;

    private UUID takenByTeacherId;
    private String takenByTeacherName;

    private Set<ExamResultSummaryMinimalDto> results;
}
