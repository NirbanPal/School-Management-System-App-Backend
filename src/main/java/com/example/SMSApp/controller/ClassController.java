package com.example.SMSApp.controller;


import com.example.SMSApp.dto.request.ClassRequestDto;
import com.example.SMSApp.dto.response.ClassResponseDto;
import com.example.SMSApp.service.ClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/class")
@RequiredArgsConstructor
@Slf4j
public class ClassController {
    private final ClassService classService;

    @GetMapping
    public ResponseEntity<List<ClassResponseDto>> getAllClasses() {
        return ResponseEntity.ok(classService.getAllClass());
    }

    @PostMapping
    public ResponseEntity<ClassResponseDto> createClass(@RequestBody @Valid ClassRequestDto gradeRequestDto) {
        log.info("Creating grade controller working");
        return ResponseEntity.status(HttpStatus.CREATED).body(classService.createClass(gradeRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassResponseDto> updateClass(
            @PathVariable UUID id,
            @RequestBody @Valid ClassRequestDto dto) {
        log.info("Updating grade with id {}", id);
        return ResponseEntity.ok(classService.updateClass(id, dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClassResponseDto> getClass(@PathVariable UUID id) {
        return ResponseEntity.ok(classService.getClassById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable UUID id) {
        classService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }
}
