package com.example.SMSApp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AssignmentRequestDto {

    @NotBlank(message = "Assignment title is required")
    private String title;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDateTime startDate;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;

    @NotNull(message = "Lesson ID is required")
    private UUID lessonId;

    @NotNull(message = "Teacher ID is required")
    private UUID takenByTeacherId;

}
