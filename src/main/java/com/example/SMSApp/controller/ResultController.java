package com.example.SMSApp.controller;

import com.example.SMSApp.dto.request.ResultRequestDto;
import com.example.SMSApp.dto.response.ResultResponseDto;
import com.example.SMSApp.service.ResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/result")
@RequiredArgsConstructor
@Slf4j
public class ResultController {

    private final ResultService resultService;

    @GetMapping
    public ResponseEntity<List<ResultResponseDto>> getAllResults() {
        return ResponseEntity.ok(resultService.getAllResults());
    }

    @PostMapping
    public ResponseEntity<ResultResponseDto> createResult(@RequestBody @Valid ResultRequestDto resultRequestDto) {
        log.info("Creating result controller working");
        return ResponseEntity.status(HttpStatus.CREATED).body(resultService.createResult(resultRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponseDto> updateResult(
            @PathVariable UUID id,
            @RequestBody @Valid ResultRequestDto dto) {
        log.info("Updating result with id {}", id);
        return ResponseEntity.ok(resultService.updateResult(id, dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResultResponseDto> getResultById(@PathVariable UUID id) {
        return ResponseEntity.ok(resultService.getResultById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable UUID id) {
        resultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }

}
