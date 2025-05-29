package com.example.SMSApp.controller;

import com.example.SMSApp.dto.request.TeacherRequestDto;
import com.example.SMSApp.dto.response.TeacherResponseDto;
import com.example.SMSApp.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
@Slf4j
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<List<TeacherResponseDto>> getAll() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @PostMapping
    public ResponseEntity<TeacherResponseDto> create(@RequestPart("data") @Valid TeacherRequestDto teacherRequestDto,
                                                     @RequestPart("cv") MultipartFile cv,
                                                     @RequestPart("idFile") MultipartFile idFile,
                                                     @RequestPart("profilePic") MultipartFile profilePic) {
        log.info("Creating Teacher controller working");
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.createTeacher(teacherRequestDto,cv,idFile,profilePic));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> updateTeacher(
            @PathVariable UUID id,
            @RequestPart("data") @Valid TeacherRequestDto dto,
            @RequestPart(value = "cv", required = false) MultipartFile cv,
            @RequestPart(value = "idFile", required = false) MultipartFile idFile,
            @RequestPart(value = "profilePic", required = false) MultipartFile profilePic) {
        log.info("Updating teacher with id {}", id);
        return ResponseEntity.ok(teacherService.updateTeacher(id, dto, cv, idFile, profilePic));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approve(@PathVariable UUID id) {
        teacherService.approveTeacherRegistration(id);
        return ResponseEntity.ok("Approved successfully and emp generated. ");
    }

    @DeleteMapping("/{id}/registration")
    public ResponseEntity<Void> deleteRegistration(@PathVariable UUID id) {
        teacherService.deleteTeacherRegistration(id);
        return ResponseEntity.noContent().build();
    }
}
