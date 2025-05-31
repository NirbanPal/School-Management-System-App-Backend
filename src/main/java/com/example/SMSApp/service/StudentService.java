package com.example.SMSApp.service;


import com.example.SMSApp.dto.request.StudentRequestDto;
import com.example.SMSApp.dto.response.StudentResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    List<StudentResponseDto> getAllStudents();

    StudentResponseDto getStudentById(UUID publicId);

    StudentResponseDto createStudent(StudentRequestDto studentRequestDto, MultipartFile idFile, MultipartFile profilePic);

    StudentResponseDto updateStudent(UUID id, StudentRequestDto studentRequestDto, MultipartFile idFile, MultipartFile profilePic);

    void deleteStudent(UUID publicId);

    void approveStudentRegistration(UUID publicId);

    void deleteStudentRegistration(UUID publicId);
}
