package com.example.SMSApp.mapper;

import com.example.SMSApp.dto.request.LessonRequestDto;
import com.example.SMSApp.dto.response.LessonResponseDto;
import com.example.SMSApp.model.Lesson;

import java.util.stream.Collectors;

public class LessonMapper {

    public static LessonResponseDto toLessonDto(Lesson lesson) {
        if (lesson == null) return null;

        return LessonResponseDto.builder()
                .publicId(lesson.getPublicId())
                .name(lesson.getName())
                .day(lesson.getDay())
                .startTime(lesson.getStartTime())
                .endTime(lesson.getEndTime())
                .subjectPublicId(lesson.getSubject().getPublicId())
                .subjectName(lesson.getSubject().getName())
                .classPublicId(lesson.getClassEntity().getPublicId())
                .className(lesson.getClassEntity().getName())
                .teacherPublicId(lesson.getTeacher().getPublicId())
                .teacherFullName(lesson.getTeacher().getPersonInfo().getFullName()) // assuming you have getFullName()
                .build();
    }

    public static Lesson toLessonEntity(LessonRequestDto dto, Lesson existingLesson) {
        if (dto == null) return null;

        Lesson lesson = existingLesson != null ? existingLesson : new Lesson();

        lesson.setName(dto.getName());
        lesson.setDay(dto.getDay());
        lesson.setStartTime(dto.getStartTime());
        lesson.setEndTime(dto.getEndTime());

        return lesson;

    }
}
