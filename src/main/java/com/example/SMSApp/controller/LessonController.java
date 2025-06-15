package com.example.SMSApp.controller;


import com.example.SMSApp.dto.request.LessonRequestDto;
import com.example.SMSApp.dto.response.LessonResponseDto;
import com.example.SMSApp.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lesson")
@RequiredArgsConstructor
@Slf4j
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<LessonResponseDto>> getAllLessons() {
        return ResponseEntity.ok(lessonService.getAllLessons());
    }

    @PostMapping
    public ResponseEntity<LessonResponseDto> createLesson(@RequestBody @Valid LessonRequestDto lessonRequestDto) {
        log.info("Creating lesson controller working");
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.createLesson(lessonRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponseDto> updateLesson(
            @PathVariable UUID id,
            @RequestBody @Valid LessonRequestDto dto) {
        log.info("Updating lesson with id {}", id);
        return ResponseEntity.ok(lessonService.updateLesson(id, dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<LessonResponseDto> getLessonById(@PathVariable UUID id) {
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable UUID id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}
