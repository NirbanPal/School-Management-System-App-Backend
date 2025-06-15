package com.example.SMSApp.controller;


import com.example.SMSApp.dto.request.SubjectRequestDto;
import com.example.SMSApp.dto.response.SubjectResponseDto;
import com.example.SMSApp.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subject")
@RequiredArgsConstructor
@Slf4j
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<SubjectResponseDto>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @PostMapping
    public ResponseEntity<SubjectResponseDto> createSubject(@RequestBody @Valid SubjectRequestDto subjectRequestDto) {
        log.info("Creating subject controller working");
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.createSubject(subjectRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponseDto> updateSubject(
            @PathVariable UUID id,
            @RequestBody @Valid SubjectRequestDto dto) {
        log.info("Updating subject with id {}", id);
        return ResponseEntity.ok(subjectService.updateSubject(id, dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDto> getSubjectById(@PathVariable UUID id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable UUID id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }

}
