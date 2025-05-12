package com.example.SMSApp.controller;

import com.example.SMSApp.model.AppUser;
import com.example.SMSApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}

