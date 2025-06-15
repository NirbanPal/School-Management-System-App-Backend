package com.example.SMSApp.service;

import com.example.SMSApp.dto.request.ResultRequestDto;
import com.example.SMSApp.dto.response.ResultResponseDto;

import java.util.List;
import java.util.UUID;

public interface ResultService {

    List<ResultResponseDto> getAllResults();

    ResultResponseDto createResult(ResultRequestDto resultRequestDto);

    ResultResponseDto updateResult(UUID id, ResultRequestDto dto);

    ResultResponseDto getResultById(UUID id);

    void deleteResult(UUID id);

}
