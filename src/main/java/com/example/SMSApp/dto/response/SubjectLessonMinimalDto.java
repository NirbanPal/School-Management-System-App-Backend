package com.example.SMSApp.dto.response;

import jakarta.persistence.Column;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectLessonMinimalDto {

    private UUID publicId;
    private String name;

}
