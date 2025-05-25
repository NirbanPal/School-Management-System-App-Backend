package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.OtpSessionDto;
import com.example.SMSApp.dto.request.AuthRequest;
import com.example.SMSApp.dto.response.AuthResponse;
import com.example.SMSApp.model.AppUser;
import com.example.SMSApp.model.enums.Role;
import com.example.SMSApp.repository.UserRepository;
import com.example.SMSApp.security.JwtService;
import com.example.SMSApp.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.SMSApp.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final RedisTemplate<String, OtpSessionDto> otpRedisTemplate;

    @Override
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

    @Override
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

    @Override
    public String forgetPasswordService(AuthRequest request) {
        String email=request.getEmail();
        AppUser user=userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email is not registered. Please register."));


        if(!otpService.isEmailVerified(email))
            throw new RuntimeException("Email has not been validated.");

        if(otpRedisTemplate.opsForValue().get("otp-session:" + email)==null)
            throw new RuntimeException("Session Expired. Please try again.");


        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        otpService.deleteOtp(email);


        return "Password Reset Successful.";
    }

}
