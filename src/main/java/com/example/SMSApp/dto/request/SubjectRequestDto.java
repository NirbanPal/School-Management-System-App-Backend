package com.example.SMSApp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SubjectRequestDto {

    @NotBlank(message = "Subject is required with subject code")
    @Size(max = 50, message = "Name must be at most 50 characters.")
    private String subjectName;


}
