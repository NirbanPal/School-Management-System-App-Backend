package com.example.SMSApp.service;

import com.example.SMSApp.dto.request.TeacherRequestDto;
import com.example.SMSApp.dto.response.TeacherResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface TeacherService {

    List<TeacherResponseDto> getAllTeachers();

    TeacherResponseDto getTeacherById(UUID publicId);

    TeacherResponseDto createTeacher(TeacherRequestDto teacherRequestDto, MultipartFile cv, MultipartFile idFile, MultipartFile profilePic);

    TeacherResponseDto updateTeacher(UUID id, TeacherRequestDto dto, MultipartFile cv, MultipartFile idFile, MultipartFile profilePic);

    void deleteTeacher(UUID publicId);

    void approveTeacherRegistration(UUID publicId);

    void deleteTeacherRegistration(UUID publicId);
}
