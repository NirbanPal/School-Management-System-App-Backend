package com.example.SMSApp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ResultRequestDto {

    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score cannot be negative")
    private Integer score;

    // Accept either examId or assignmentId (but not both)
    private UUID examId;

    private UUID assignmentId;

    @NotNull(message = "Student ID is required")
    private UUID studentId;

}
