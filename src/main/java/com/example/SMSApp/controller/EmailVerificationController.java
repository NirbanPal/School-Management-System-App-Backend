package com.example.SMSApp.controller;


import com.example.SMSApp.dto.request.OtpRequestDto;
import com.example.SMSApp.dto.request.OtpVerifyDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.model.AppUser;
import com.example.SMSApp.model.Student;
import com.example.SMSApp.model.enums.Role;
import com.example.SMSApp.repository.StudentRepository;
import com.example.SMSApp.repository.UserRepository;
import com.example.SMSApp.support.email.EmailService;
import com.example.SMSApp.support.otp.OtpService;
import com.example.SMSApp.support.ratelimit.RateLimitingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class EmailVerificationController {
    private final OtpService otpService;
    private final RateLimitingService rateLimitingService;
    @Autowired
    private final EmailService emailService;

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody @Valid OtpRequestDto emailDetails, HttpServletRequest request) {
        String email= emailDetails.getEmail();
        String clientIp = request.getRemoteAddr();
        if (!rateLimitingService.isAllowedIp(clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("IP limit exceeded. Try again after 1 hour.");
        }

        if (!otpService.canSendOtp(email)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Email limit exceeded or cooldown in progress. Try again later.");
        }

        if(emailDetails.getRole()==Role.STUDENT){
            AppUser user=userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Email is not registered. Please Contact with admin."));
            Student student=studentRepository.findByAppUser(user).orElseThrow(()->new ResourceNotFoundException("Student does not found"));
            email=student.getParent().getAppUser().getEmail();
        }
        String otp = String.valueOf(new Random().nextInt(899999) + 100000);
        otpService.storeOtp(email, otp);

        // Sending OTP to mail
        emailService.sendEmail(email,"OTP Verification From SMS APP","Your OTP is"+otp);


        return ResponseEntity.ok("If your mail is valid. OTP sent successfully. "+ otp);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody @Valid OtpVerifyDto otpVerify) {
        boolean verified = otpService.verifyOtp(otpVerify.getEmail(), otpVerify.getOtp());
        if (verified) return ResponseEntity.ok("Email verified successfully.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP.");
    }


}

