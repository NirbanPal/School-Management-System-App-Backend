package com.example.SMSApp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeRequestDto {


    @NotNull(message = "Grade level must not be null")
//    @Positive(message = "Grade level must be a positive number")
    @Min(value = 1, message = "Grade level must be at least 1")
    @Max(value = 12, message = "Grade level must not exceed 14")
    private Integer level;
}
