package com.example.SMSApp.dto.response;


import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectResponseDto {
    private UUID publicId;
    private String name;
    private List<SubjectTeacherMinimalDto> teachers;
    private List<SubjectLessonMinimalDto> lessons;

}
