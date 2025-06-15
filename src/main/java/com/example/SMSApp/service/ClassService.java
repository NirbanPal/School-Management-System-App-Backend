package com.example.SMSApp.service;

import com.example.SMSApp.dto.request.ClassRequestDto;
import com.example.SMSApp.dto.response.ClassResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ClassService {

    List<ClassResponseDto> getAllClass();

    ClassResponseDto getClassById(UUID publicId);

    ClassResponseDto createClass(ClassRequestDto classRequestDto);

    ClassResponseDto updateClass(UUID id, ClassRequestDto dto);

    void deleteClass(UUID publicId);
}
