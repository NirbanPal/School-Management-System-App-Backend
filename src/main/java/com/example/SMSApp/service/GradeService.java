package com.example.SMSApp.service;

import com.example.SMSApp.dto.request.GradeRequestDto;
import com.example.SMSApp.dto.response.GradeResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;
import java.util.UUID;

public interface GradeService {

    List<GradeResponseDto> getAllGrades();

    GradeResponseDto createGrade(GradeRequestDto gradeRequestDto);

    GradeResponseDto updateGrade(UUID id, GradeRequestDto dto);

    GradeResponseDto getGradeById(UUID id);

    void deleteGrade(UUID id);
}
