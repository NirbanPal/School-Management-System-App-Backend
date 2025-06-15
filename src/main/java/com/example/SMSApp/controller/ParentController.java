package com.example.SMSApp.controller;

import com.example.SMSApp.dto.request.ParentRequestDto;
import com.example.SMSApp.dto.response.ParentResponseDto;
import com.example.SMSApp.service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;



@RestController
@RequestMapping("/api/v1/parents")
@RequiredArgsConstructor
@Slf4j
public class ParentController {

    private final ParentService parentService;

    @GetMapping
    public ResponseEntity<List<ParentResponseDto>> getAllParents() {
        return ResponseEntity.ok(parentService.getAllParents());
    }

    @PostMapping
    public ResponseEntity<ParentResponseDto> createParent(@RequestPart("data") @Valid ParentRequestDto parentRequestDto,
                                                     @RequestPart("idFile") MultipartFile idFile,
                                                     @RequestPart("profilePic") MultipartFile profilePic) {
        log.info("Creating parent controller working");
        return ResponseEntity.status(HttpStatus.CREATED).body(parentService.createParent(parentRequestDto,idFile,profilePic));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParentResponseDto> updateParent(
            @PathVariable UUID id,
            @RequestPart("data") @Valid ParentRequestDto dto,
            @RequestPart(value = "idFile", required = false) MultipartFile idFile,
            @RequestPart(value = "profilePic", required = false) MultipartFile profilePic) {
        log.info("Updating parent with id {}", id);
        return ResponseEntity.ok(parentService.updateParent(id, dto, idFile, profilePic));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ParentResponseDto> getParent(@PathVariable UUID id) {
        return ResponseEntity.ok(parentService.getParentById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParent(@PathVariable UUID id) {
        parentService.deleteParent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveParent(@PathVariable UUID id) {
        parentService.approveParentRegistration(id);
        return ResponseEntity.ok("Approved successfully and emp generated. ");
    }

    @DeleteMapping("/{id}/registration")
    public ResponseEntity<Void> deleteParentRegistration(@PathVariable UUID id) {
        parentService.deleteParentRegistration(id);
        return ResponseEntity.noContent().build();
    }
}
