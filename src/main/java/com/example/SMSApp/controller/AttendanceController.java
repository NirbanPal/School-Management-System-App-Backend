package com.example.SMSApp.controller;


import com.example.SMSApp.dto.request.AttendanceRequestDto;
import com.example.SMSApp.dto.response.AttendanceResponseDto;
import com.example.SMSApp.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
@Slf4j
public class AttendanceController {


    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<AttendanceResponseDto> createAttendance(@RequestBody @Valid AttendanceRequestDto attendanceRequestDto) {
        log.info("Creating attendance controller working");
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.createAttendance(attendanceRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceResponseDto> updateAttendance(
            @PathVariable UUID id,
            @RequestBody @Valid AttendanceRequestDto dto) {
        log.info("Updating attendance with id {}", id);
        return ResponseEntity.ok(attendanceService.updateAttendance(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable UUID id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }
}
