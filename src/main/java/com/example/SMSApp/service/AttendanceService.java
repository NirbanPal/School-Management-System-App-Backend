package com.example.SMSApp.service;

import com.example.SMSApp.dto.request.AttendanceRequestDto;
import com.example.SMSApp.dto.response.AttendanceResponseDto;

import java.util.List;
import java.util.UUID;

public interface AttendanceService {

//    List<AttendanceResponseDto> getAllAttendance();

//    AttendanceResponseDto getAttendanceById(UUID publicId);

    AttendanceResponseDto createAttendance(AttendanceRequestDto attendanceRequestDto);

    AttendanceResponseDto updateAttendance(UUID id, AttendanceRequestDto dto);

    void deleteAttendance(UUID publicId);

}
