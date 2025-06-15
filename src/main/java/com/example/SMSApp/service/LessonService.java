package com.example.SMSApp.service;

import com.example.SMSApp.dto.request.LessonRequestDto;
import com.example.SMSApp.dto.request.LessonRequestDto;
import com.example.SMSApp.dto.response.LessonResponseDto;
import com.example.SMSApp.dto.response.LessonResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface LessonService {


    List<LessonResponseDto> getAllLessons();

    LessonResponseDto createLesson(LessonRequestDto lessonRequestDto);

    LessonResponseDto updateLesson(UUID id, LessonRequestDto dto);

    LessonResponseDto getLessonById(UUID id);

    void deleteLesson(UUID id);



}
