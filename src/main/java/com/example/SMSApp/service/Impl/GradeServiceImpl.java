package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.request.GradeRequestDto;
import com.example.SMSApp.dto.response.GradeResponseDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.GradeMapper;
import com.example.SMSApp.mapper.ParentMapper;
import com.example.SMSApp.model.Grade;
import com.example.SMSApp.repository.GradeRepository;
import com.example.SMSApp.repository.ParentRepository;
import com.example.SMSApp.service.GradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    @Override
    public List<GradeResponseDto> getAllGrades() {
        return gradeRepository.findAll()
                .stream()
                .map(GradeMapper::toGradeDto)
                .collect(Collectors.toList());
    }

    @Override
    public GradeResponseDto createGrade(GradeRequestDto gradeRequestDto) {
        Grade grade= GradeMapper.toGradeEntity(gradeRequestDto,null);
        Grade saved=gradeRepository.save(grade);
        return GradeMapper.toGradeDto(saved);
    }

    @Override
    public GradeResponseDto updateGrade(UUID id, GradeRequestDto dto) {
        Grade grade=gradeRepository.findByPublicId(id).orElseThrow(()-> new ResourceNotFoundException("Grade not found."));
        Grade gradeObj=GradeMapper.toGradeEntity(dto,grade);
        return GradeMapper.toGradeDto(gradeRepository.save(gradeObj));

    }

    @Override
    public GradeResponseDto getGradeById(UUID id) {
        Grade grade=gradeRepository.findByPublicId(id).orElseThrow(() -> new ResourceNotFoundException("Grade not found"));
        return GradeMapper.toGradeDto(grade);
    }

    @Override
    public void deleteGrade(UUID id) {
        Grade grade=gradeRepository.findByPublicId(id).orElseThrow(() -> new ResourceNotFoundException("Grade not found"));
        gradeRepository.delete(grade);
    }
}
