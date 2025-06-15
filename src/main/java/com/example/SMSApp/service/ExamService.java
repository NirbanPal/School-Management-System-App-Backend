package com.example.SMSApp.service;

import com.example.SMSApp.dto.request.ExamRequestDto;
import com.example.SMSApp.dto.response.ExamResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface ExamService {

    List<ExamResponseDto> getAllExams();

    ExamResponseDto createExam(ExamRequestDto examRequestDto);

    ExamResponseDto updateExam(UUID id, ExamRequestDto dto);

    ExamResponseDto getExamById(UUID id);

    void deleteExam(UUID id);
}
