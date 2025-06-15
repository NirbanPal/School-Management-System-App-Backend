package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.request.OtpRequestDto;
import com.example.SMSApp.dto.request.OtpVerifyDto;
import com.example.SMSApp.exception.custom.BadRequestException;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.model.AppUser;
import com.example.SMSApp.model.Student;
import com.example.SMSApp.model.enums.Role;
import com.example.SMSApp.repository.StudentRepository;
import com.example.SMSApp.repository.UserRepository;
import com.example.SMSApp.service.EmailVerificationService;
import com.example.SMSApp.support.email.EmailService;
import com.example.SMSApp.support.otp.OtpService;
import com.example.SMSApp.support.ratelimit.RateLimitingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationServiceImpl implements EmailVerificationService {


    private final RateLimitingService rateLimitingService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;


    @Override
    public String sendOtp(OtpRequestDto emailDetails, HttpServletRequest request) {


        String email= emailDetails.getEmail();

        String clientIp = request.getRemoteAddr();
        if (!rateLimitingService.isAllowedIp(clientIp)) {
            throw new BadRequestException("IP limit exceeded. Try again after 1 hour.");
        }

        if (!otpService.canSendOtp(email)) {
            throw new BadRequestException("Email limit exceeded or cooldown in progress. Try again later.");
        }

        // If role is STUDENT, send OTP to parent's email
        if (Role.STUDENT.name().equalsIgnoreCase(String.valueOf(emailDetails.getRole()))) {
            AppUser user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Email is not registered. Please contact admin."));

            Student student = studentRepository.findByAppUser(user)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found."));

            email = student.getParent().getAppUser().getEmail(); // override email
        }

        String otp = String.valueOf(new Random().nextInt(899999) + 100000);
        otpService.storeOtp(email, otp);

        emailService.sendEmail(email, "OTP Verification From SMS APP", "Your OTP is " + otp);

        return "If your mail is valid. OTP sent successfully. " + otp;
    }

    @Override
    public boolean verifyOtp(OtpVerifyDto otpVerify) {
        return otpService.verifyOtp(otpVerify.getEmail(), otpVerify.getOtp());
    }
}
