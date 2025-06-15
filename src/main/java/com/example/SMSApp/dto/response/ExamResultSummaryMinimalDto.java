package com.example.SMSApp.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ExamResultSummaryMinimalDto {
    private UUID publicId;
    private String studentRollNo;
    private String studentName;
    private Integer score;

}
