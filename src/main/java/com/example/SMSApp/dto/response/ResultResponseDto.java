package com.example.SMSApp.dto.response;

import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultResponseDto {

    private UUID publicId;

    private Integer score;

    private UUID examId;
    private String examTitle;

    private UUID assignmentId;
    private String assignmentTitle;

    private UUID studentId;
    private String studentFullName;
}
