package com.example.SMSApp.mapper;

import com.example.SMSApp.dto.request.ResultRequestDto;
import com.example.SMSApp.dto.response.ResultResponseDto;
import com.example.SMSApp.model.PersonInfo;
import com.example.SMSApp.model.Result;

public class ResultMapper {

    public static ResultResponseDto toResultDto(Result result) {
        if (result == null) return null;


        return ResultResponseDto.builder()
                .publicId(result.getPublicId())
                .score(result.getScore())
                .studentId(result.getStudent().getPublicId())
                .studentFullName(result.getStudent().getPersonInfo().getFullName())

                .examId(result.getExam() != null ? result.getExam().getPublicId() : null)
                .examTitle(result.getExam() != null ? result.getExam().getTitle() : null)

                .assignmentId(result.getAssignment() != null ? result.getAssignment().getPublicId() : null)
                .assignmentTitle(result.getAssignment() != null ? result.getAssignment().getTitle() : null)
                .build();
    }

    public static Result toResultEntity(ResultRequestDto dto, Result existingResult) {
        if (dto == null) return null;

        Result result = existingResult != null ? existingResult : new Result();

        result.setScore(dto.getScore());


        return result;

    }
}
