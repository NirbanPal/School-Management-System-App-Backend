package com.example.SMSApp.dto.response;

import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassResponseDto {

    private UUID publicId;

    private String name;

    private Integer capacity;

    private String supervisorName; // e.g., "Mr. John Doe"

    private Integer gradeLevel;

    private int studentCount;
}
