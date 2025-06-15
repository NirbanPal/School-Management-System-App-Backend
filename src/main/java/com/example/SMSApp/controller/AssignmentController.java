package com.example.SMSApp.controller;

import com.example.SMSApp.dto.request.AssignmentRequestDto;
import com.example.SMSApp.dto.response.AssignmentResponseDto;
import com.example.SMSApp.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;



@RestController
@RequestMapping("/api/v1/assignment")
@RequiredArgsConstructor
@Slf4j
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<List<AssignmentResponseDto>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @PostMapping
    public ResponseEntity<AssignmentResponseDto> createAssignment(@RequestBody @Valid AssignmentRequestDto assignmentRequestDto) {
        log.info("Creating assignment controller working");
        return ResponseEntity.status(HttpStatus.CREATED).body(assignmentService.createAssignment(assignmentRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentResponseDto> updateAssignment(
            @PathVariable UUID id,
            @RequestBody @Valid AssignmentRequestDto dto) {
        log.info("Updating assignment with id {}", id);
        return ResponseEntity.ok(assignmentService.updateAssignment(id, dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponseDto> getAssignmentById(@PathVariable UUID id) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable UUID id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
