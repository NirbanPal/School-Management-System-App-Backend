package com.example.SMSApp.service;

import com.example.SMSApp.dto.request.AssignmentRequestDto;
import com.example.SMSApp.dto.response.AssignmentResponseDto;

import java.util.List;
import java.util.UUID;

public interface AssignmentService {

    List<AssignmentResponseDto> getAllAssignments();

    AssignmentResponseDto createAssignment(AssignmentRequestDto assignmentRequestDto);

    AssignmentResponseDto updateAssignment(UUID id, AssignmentRequestDto dto);

    AssignmentResponseDto getAssignmentById(UUID id);

    void deleteAssignment(UUID id);
}
