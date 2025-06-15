package com.example.SMSApp.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectTeacherMinimalDto {
    private UUID publicId;
    private String name;
    private String surname;
    private String qualification;
}
