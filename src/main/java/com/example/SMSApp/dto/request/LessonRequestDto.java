package com.example.SMSApp.dto.request;

import com.example.SMSApp.model.enums.Day;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class LessonRequestDto {

    @NotBlank(message = "Lesson name is required.")
    @Size(max = 100, message = "Lesson name must not exceed 100 characters.")
    private String name;

    @NotNull(message = "Day is required.")
    private Day day;

    @NotNull(message = "Start time is required.")
    @FutureOrPresent(message = "Start time must be in the present or future.")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required.")
    @Future(message = "End time must be in the future.")
    private LocalDateTime endTime;

    @NotNull(message = "Subject ID is required.")
    private UUID subjectId;

    @NotNull(message = "Class ID is required.")
    private UUID classId;

    @NotNull(message = "Teacher ID is required.")
    private UUID teacherId;

}
