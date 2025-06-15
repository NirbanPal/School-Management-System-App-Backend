package com.example.SMSApp.service;

import com.example.SMSApp.dto.request.SubjectRequestDto;
import com.example.SMSApp.dto.response.SubjectResponseDto;
import com.example.SMSApp.dto.response.SubjectResponseDto;

import java.util.List;
import java.util.UUID;

public interface SubjectService {

        List<SubjectResponseDto> getAllSubjects();

        SubjectResponseDto createSubject(SubjectRequestDto subjectRequestDto);

        SubjectResponseDto updateSubject(UUID id, SubjectRequestDto subjectRequestDto);

        SubjectResponseDto getSubjectById(UUID id);

        void deleteSubject(UUID id);

}
