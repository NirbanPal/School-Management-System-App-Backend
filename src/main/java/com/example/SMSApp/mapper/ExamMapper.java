package com.example.SMSApp.mapper;

import com.example.SMSApp.dto.request.ExamRequestDto;
import com.example.SMSApp.dto.response.ExamResponseDto;
import com.example.SMSApp.dto.response.ExamResultSummaryMinimalDto;
import com.example.SMSApp.model.Exam;
import com.example.SMSApp.model.Result;

import java.util.Set;
import java.util.stream.Collectors;

public class ExamMapper {

    private static Set<ExamResultSummaryMinimalDto> mapToResultSummaryMinimalDtoSet(Set<Result> results) {
        if (results == null) return Set.of();
        return results.stream()
                .map(result -> ExamResultSummaryMinimalDto.builder()
                        .publicId(result.getPublicId())
                        .studentRollNo(result.getStudent().getRollNumber())
                        .studentName(result.getStudent().getPersonInfo().getFullName())
                        .score(result.getScore())
                        .build())
                .collect(Collectors.toSet());
    }

    public static ExamResponseDto toExamDto(Exam exam) {
        if (exam == null) return null;


        return ExamResponseDto.builder()
                .publicId(exam.getPublicId())
                .title(exam.getTitle())
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .lessonId(exam.getLesson().getPublicId())
                .lessonName(exam.getLesson().getName())
                .takenByTeacherId(exam.getTakenBy() != null ? exam.getTakenBy().getPublicId() : null)
                .takenByTeacherName(exam.getTakenBy() != null ? exam.getTakenBy().getPersonInfo().getFullName() : null)
                .results(mapToResultSummaryMinimalDtoSet(exam.getResults()))
                .build();
    }

    public static Exam toExamEntity(ExamRequestDto dto, Exam existingExam) {
        if (dto == null) return null;

        Exam exam = existingExam != null ? existingExam : new Exam();

        exam.setTitle(dto.getTitle());
        exam.setStartTime(dto.getStartTime());
        exam.setEndTime(dto.getEndTime());

        return exam;

    }
}
