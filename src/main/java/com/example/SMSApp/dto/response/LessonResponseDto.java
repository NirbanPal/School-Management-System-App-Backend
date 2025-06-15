package com.example.SMSApp.dto.response;


import com.example.SMSApp.model.enums.Day;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonResponseDto {

    private UUID publicId;

    private String name;

    private Day day;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    // Associated Entity Public IDs for linking
    private UUID subjectPublicId;
    private String subjectName;

    private UUID classPublicId;
    private String className;

    private UUID teacherPublicId;
    private String teacherFullName;
}
