package com.example.SMSApp.controller;


import com.example.SMSApp.dto.request.GradeRequestDto;
import com.example.SMSApp.dto.response.GradeResponseDto;
import com.example.SMSApp.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/grade")
@RequiredArgsConstructor
@Slf4j
public class GradeController {
    private final GradeService gradeService;

    @GetMapping
    public ResponseEntity<List<GradeResponseDto>> getAllGrades() {
        return ResponseEntity.ok(gradeService.getAllGrades());
    }

    @PostMapping
    public ResponseEntity<GradeResponseDto> createGrade(@RequestBody @Valid GradeRequestDto gradeRequestDto) {
        log.info("Creating grade controller working");
        return ResponseEntity.status(HttpStatus.CREATED).body(gradeService.createGrade(gradeRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeResponseDto> updateGrade(
            @PathVariable UUID id,
            @RequestBody @Valid GradeRequestDto dto) {
        log.info("Updating grade with id {}", id);
        return ResponseEntity.ok(gradeService.updateGrade(id, dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<GradeResponseDto> getGrade(@PathVariable UUID id) {
        return ResponseEntity.ok(gradeService.getGradeById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable UUID id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}
