package com.example.SMSApp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassRequestDto {

    @NotBlank(message = "Class name must not be blank")
    @Size(min = 1, max = 50, message = "Class name must be between 1 and 50 characters")
    private String name;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @NotNull(message = "Supervisor (teacher) ID is required")
    private UUID teacherId;

    @NotNull(message = "Grade ID is required")
    private UUID gradeId;
}
