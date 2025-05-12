package com.example.SMSApp.controller;


import com.example.SMSApp.dto.OtpRequestDto;
import com.example.SMSApp.dto.OtpVerifyDto;
import com.example.SMSApp.service.EmailService;
import com.example.SMSApp.service.OtpService;
import com.example.SMSApp.service.RateLimitingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class EmailVerificationController {
    private final OtpService otpService;
    private final RateLimitingService rateLimitingService;
    @Autowired
    private final EmailService emailService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody @Valid OtpRequestDto email, HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        if (!rateLimitingService.isAllowedIp(clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("IP limit exceeded. Try again after 1 hour.");
        }
        System.out.println("Running");

        if (!otpService.canSendOtp(email.getEmail())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Email limit exceeded or cooldown in progress. Try again later.");
        }

        String otp = String.valueOf(new Random().nextInt(899999) + 100000);
        otpService.storeOtp(email.getEmail(), otp);

        // Sending OTP to mail
        emailService.sendEmail(email.getEmail(),"OTP Verification From SMS APP",otp);


        return ResponseEntity.ok("If your mail is valid. OTP sent successfully. "+ otp);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody @Valid OtpVerifyDto otpVerify) {
        boolean verified = otpService.verifyOtp(otpVerify.getEmail(), otpVerify.getOtp());
        if (verified) return ResponseEntity.ok("Email verified successfully.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP.");
    }


}

