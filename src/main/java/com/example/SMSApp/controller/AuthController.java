package com.example.SMSApp.controller;

import com.example.SMSApp.dto.AuthRequest;
import com.example.SMSApp.dto.AuthResponse;
import com.example.SMSApp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
