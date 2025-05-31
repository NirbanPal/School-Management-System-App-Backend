package com.example.SMSApp.controller;


import com.example.SMSApp.dto.request.StudentRequestDto;
import com.example.SMSApp.dto.response.StudentResponseDto;
import com.example.SMSApp.service.ParentService;
import com.example.SMSApp.service.StudentService;
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
@RequestMapping("/api/v1/parents")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(@RequestPart("data") @Valid StudentRequestDto studentRequestDto,
                                                          @RequestPart("idFile") MultipartFile idFile,
                                                          @RequestPart("profilePic") MultipartFile profilePic) {
        log.info("Creating parent controller working");
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(studentRequestDto,idFile,profilePic));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(
            @PathVariable UUID id,
            @RequestPart("data") @Valid StudentRequestDto dto,
            @RequestPart(value = "idFile", required = false) MultipartFile idFile,
            @RequestPart(value = "profilePic", required = false) MultipartFile profilePic) {
        log.info("Updating parent with id {}", id);
        return ResponseEntity.ok(studentService.updateStudent(id, dto, idFile, profilePic));
    }


    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudent(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveStudent(@PathVariable UUID id) {
        studentService.approveStudentRegistration(id);
        return ResponseEntity.ok("Approved successfully and emp generated. ");
    }

    @DeleteMapping("/{id}/registration")
    public ResponseEntity<Void> deleteStudentRegistration(@PathVariable UUID id) {
        studentService.deleteStudentRegistration(id);
        return ResponseEntity.noContent().build();
    }
}
