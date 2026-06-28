package com.example.SMSApp.mapper;

import com.example.SMSApp.dto.request.GradeRequestDto;
import com.example.SMSApp.dto.response.GradeResponseDto;
import com.example.SMSApp.entity.ClassEntity;
import com.example.SMSApp.entity.Grade;

public class GradeMapper {

    public static GradeResponseDto toGradeDto(Grade grade) {
        if (grade == null) return null;

        return GradeResponseDto.builder().publicId(grade.getPublicId())
                .level(grade.getLevel())
                .classNames(grade.getClasses().stream().map(ClassEntity::getName).toList())
                .build();

    }

    public static Grade toGradeEntity(GradeRequestDto dto, Grade existingGrade) {
        if (dto == null) return null;

        Grade grade = existingGrade != null ? existingGrade : new Grade();

//        grade.setPublicId(dto.getPublicId()); // if needed

        grade.setLevel(dto.getLevel());

        return grade;
    }
}
