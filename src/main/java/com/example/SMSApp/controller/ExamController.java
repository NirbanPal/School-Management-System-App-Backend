package com.example.SMSApp.controller;


import com.example.SMSApp.dto.request.ExamRequestDto;
import com.example.SMSApp.dto.response.ExamResponseDto;
import com.example.SMSApp.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
@Slf4j
public class ExamController {
    private final ExamService examService;

    @GetMapping
    public ResponseEntity<List<ExamResponseDto>> getAllExams() {
        return ResponseEntity.ok(examService.getAllExams());
    }

    @PostMapping
    public ResponseEntity<ExamResponseDto> createExam(@RequestBody @Valid ExamRequestDto examRequestDto) {
        log.info("Creating exam controller working");
        return ResponseEntity.status(HttpStatus.CREATED).body(examService.createExam(examRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamResponseDto> updateExam(
            @PathVariable UUID id,
            @RequestBody @Valid ExamRequestDto dto) {
        log.info("Updating exam with id {}", id);
        return ResponseEntity.ok(examService.updateExam(id, dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDto> getExamById(@PathVariable UUID id) {
        return ResponseEntity.ok(examService.getExamById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable UUID id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }
}
