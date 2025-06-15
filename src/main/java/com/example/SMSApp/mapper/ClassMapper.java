package com.example.SMSApp.mapper;

import com.example.SMSApp.dto.request.ClassRequestDto;
import com.example.SMSApp.dto.response.ClassResponseDto;
import com.example.SMSApp.model.ClassEntity;
import com.example.SMSApp.model.Grade;
import com.example.SMSApp.model.Teacher;

public class ClassMapper {

    public static ClassResponseDto toClassDto(ClassEntity classEntity) {
        if (classEntity == null) return null;


        return ClassResponseDto.builder()
                .publicId(classEntity.getPublicId())
                .name(classEntity.getName())
                .capacity(classEntity.getCapacity())
                .supervisorName(classEntity.getSupervisor() != null
                        ? classEntity.getSupervisor().getPersonInfo().getFullName()
                        : null)
                .gradeLevel(classEntity.getGrade() != null ? classEntity.getGrade().getLevel() : null)
                .studentCount(classEntity.getStudents() != null ? classEntity.getStudents().size() : 0)
                .build();
    }

    public static ClassEntity toClassEntity(ClassRequestDto dto, ClassEntity existingClassEntity) {
        if (dto == null) return null;

        ClassEntity classEntity = existingClassEntity != null ? existingClassEntity : new ClassEntity();


//        classEntity.setPublicId(dto.getPublicId()); // if needed
        classEntity.setName(dto.getName());
        classEntity.setCapacity(dto.getCapacity());


        return classEntity;

    }
}
