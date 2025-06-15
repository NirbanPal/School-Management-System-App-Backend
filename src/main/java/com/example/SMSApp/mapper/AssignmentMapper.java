package com.example.SMSApp.mapper;



import com.example.SMSApp.dto.request.AssignmentRequestDto;
import com.example.SMSApp.dto.response.AssignmentResponseDto;
import com.example.SMSApp.dto.response.AssignmentResultSummaryMinimalDto;
import com.example.SMSApp.model.Assignment;
import com.example.SMSApp.model.Result;

import java.util.Set;
import java.util.stream.Collectors;

public class AssignmentMapper {

    private static Set<AssignmentResultSummaryMinimalDto> mapToResultSummaryMinimalDtoSet(Set<Result> results) {
        if (results == null) return Set.of();
        return results.stream()
                .map(result -> AssignmentResultSummaryMinimalDto.builder()
                        .publicId(result.getPublicId())
                        .studentRollNo(result.getStudent().getRollNumber())
                        .studentName(result.getStudent().getPersonInfo().getFullName())
                        .score(result.getScore())
                        .build())
                .collect(Collectors.toSet());
    }

    public static AssignmentResponseDto toAssignmentDto(Assignment assignment) {
        if (assignment == null) return null;


        return AssignmentResponseDto.builder()
                .publicId(assignment.getPublicId())
                .title(assignment.getTitle())
                .startDate(assignment.getStartDate())
                .dueDate(assignment.getDueDate())
                .lessonId(assignment.getLesson().getPublicId())
                .lessonName(assignment.getLesson().getName())
                .takenByTeacherId(assignment.getTakenBy() != null ? assignment.getTakenBy().getPublicId() : null)
                .takenByTeacherName(assignment.getTakenBy() != null ? assignment.getTakenBy().getPersonInfo().getFullName() : null)
                .results(mapToResultSummaryMinimalDtoSet(assignment.getResults()))
                .build();
    }

    public static Assignment toAssignmentEntity(AssignmentRequestDto dto, Assignment existingAssignment) {
        if (dto == null) return null;

        Assignment assignment = existingAssignment != null ? existingAssignment : new Assignment();

        assignment.setTitle(dto.getTitle());
        assignment.setStartDate(dto.getStartDate());
        assignment.setDueDate(dto.getDueDate());

        return assignment;

    }
}
