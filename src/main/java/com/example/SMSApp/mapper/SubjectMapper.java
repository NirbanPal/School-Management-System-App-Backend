package com.example.SMSApp.mapper;

import com.example.SMSApp.dto.request.SubjectRequestDto;
import com.example.SMSApp.dto.response.SubjectLessonMinimalDto;
import com.example.SMSApp.dto.response.SubjectResponseDto;
import com.example.SMSApp.dto.response.SubjectTeacherMinimalDto;
import com.example.SMSApp.model.Subject;

import java.util.stream.Collectors;

public class SubjectMapper {

    public static SubjectResponseDto toSubjectDto(Subject subject) {
        if (subject == null) return null;

        return SubjectResponseDto.builder()
                .publicId(subject.getPublicId())
                .name(subject.getName())
                .teachers(subject.getTeachers().stream()
                        .map(t-> SubjectTeacherMinimalDto.builder()
                        .publicId(t.getPublicId())
                        .name(t.getPersonInfo().getName())
                        .surname(t.getPersonInfo().getSurname())
                        .qualification(t.getQualification())
                        .build())
                        .collect(Collectors.toList()))
                .lessons(subject.getLessons().stream()
                        .map(l -> SubjectLessonMinimalDto.builder()
                                .publicId(l.getPublicId())
                                .name(l.getName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static Subject toSubjectEntity(SubjectRequestDto dto, Subject existingSubject) {
        if (dto == null) return null;

        Subject subject = existingSubject != null ? existingSubject : new Subject();

        subject.setName(dto.getSubjectName());


        return subject;

    }
}
