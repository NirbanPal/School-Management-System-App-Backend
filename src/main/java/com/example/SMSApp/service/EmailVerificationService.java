package com.example.SMSApp.service;

import com.example.SMSApp.dto.request.OtpRequestDto;
import com.example.SMSApp.dto.request.OtpVerifyDto;
import jakarta.servlet.http.HttpServletRequest;

public interface EmailVerificationService {

    String sendOtp(OtpRequestDto emailDetails, HttpServletRequest request);

    boolean verifyOtp(OtpVerifyDto otpVerify);
}
