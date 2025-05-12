package com.example.SMSApp.service;

import com.example.SMSApp.dto.AuthRequest;
import com.example.SMSApp.dto.AuthResponse;
import com.example.SMSApp.dto.OtpSessionDto;
import com.example.SMSApp.repository.UserRepository;
import com.example.SMSApp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.SMSApp.model.AppUser;
import com.example.SMSApp.model.Role;

import java.net.NetworkInterface;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final RedisTemplate<String, OtpSessionDto> otpRedisTemplate;

    public String register(AuthRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }


        if(!otpService.isEmailVerified(request.getEmail()))
            throw new RuntimeException("Email is not validated.");

        if(otpRedisTemplate.opsForValue().get("otp-session:" + request.getEmail())==null)
            throw new RuntimeException("Session Expired. Please try again.");


        if(request.getRole()==null)
            request.setRole(Role.STUDENT);



        var user = AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole()) // Default role for new registrations
                .build();

        userRepository.save(user);
        otpService.deleteOtp(request.getEmail());

//        var jwtToken = jwtService.generateToken(user.getPublicId());

        return "User Registration Successful.";
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        user.updateLastLogin();
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user.getPublicId());

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
