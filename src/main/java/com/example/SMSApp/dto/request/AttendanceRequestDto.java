package com.example.SMSApp.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AttendanceRequestDto {

    @NotNull(message = "Attendance status (present/absent) is required")
    private Boolean present;

    @NotNull(message = "Student ID is required")
    private UUID studentId;

    @NotNull(message = "Lesson ID is required")
    private UUID lessonId;

}
