package com.example.SMSApp.service;

import com.example.SMSApp.dto.request.ParentRequestDto;
import com.example.SMSApp.dto.response.ParentResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ParentService {

    List<ParentResponseDto> getAllParents();

    ParentResponseDto getParentById(UUID publicId);

    ParentResponseDto createParent(ParentRequestDto parentRequestDto, MultipartFile idFile, MultipartFile profilePic);

    ParentResponseDto updateParent(UUID id, ParentRequestDto dto, MultipartFile idFile, MultipartFile profilePic);

    void deleteParent(UUID publicId);

    void approveParentRegistration(UUID publicId);

    void deleteParentRegistration(UUID publicId);
}
