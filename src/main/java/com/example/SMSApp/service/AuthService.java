package com.example.SMSApp.service;
import com.example.SMSApp.dto.request.AuthRequest;
import com.example.SMSApp.dto.response.AuthResponse;

public interface AuthService {

    String register(AuthRequest request);

    AuthResponse authenticate(AuthRequest request);

    String forgetPasswordService(AuthRequest request);
}