package com.example.SMSApp.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ExamRequestDto {

    @NotBlank(message = "Exam title is required")
    @Size(min = 3, max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotNull(message = "Start time is required")
    @FutureOrPresent(message = "Start time must be in the present or future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @NotNull(message = "Lesson ID is required")
    private UUID lessonId;

    @NotNull(message="Teacher Id is required")
    private UUID takenByTeacherId;
}
