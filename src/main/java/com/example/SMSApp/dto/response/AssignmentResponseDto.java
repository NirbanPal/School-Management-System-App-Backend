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
public class AssignmentResponseDto {

    private UUID publicId;

    private String title;

    private LocalDateTime startDate;

    private LocalDateTime dueDate;

    private UUID lessonId;
    private String lessonName;

    private UUID takenByTeacherId;
    private String takenByTeacherName;

    private Set<AssignmentResultSummaryMinimalDto> results;


}
