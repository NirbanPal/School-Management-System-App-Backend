package com.example.SMSApp.service;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.example.SMSApp.dto.OtpSessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final RedisTemplate<String, OtpSessionDto> otpRedisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;

    private static final Duration OTP_VALIDITY = Duration.ofMinutes(2);
    private static final Duration HOURLY_LIMIT_WINDOW = Duration.ofHours(1);

    public boolean canSendOtp(String email) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String otpKey = "otp-session:" + email;
        String limitKey = "otp-limit:" + email;

        if (otpRedisTemplate.hasKey(otpKey)) return false;

        String countStr = ops.get(limitKey);
        int count = countStr != null ? Integer.parseInt(countStr) : 0;
        if (count >= 5) return false;

        ops.increment(limitKey);
        if (count == 0) otpRedisTemplate.expire(limitKey, HOURLY_LIMIT_WINDOW);
        return true;
    }

    public void storeOtp(String email, String rawOtp) {

        if(otpRedisTemplate.opsForValue().get("otp-session:" + email)!=null)
            throw new RuntimeException("Please wait to generate otp again.");

        String hashedOtp = hashOtp(rawOtp);
        OtpSessionDto otpSession = OtpSessionDto.builder()
                .hashedOtp(hashedOtp)
                .isValid(false)
                .createdAt(LocalDateTime.now())
                .build();
        otpRedisTemplate.opsForValue().set("otp-session:" + email, otpSession, OTP_VALIDITY);
    }

    public boolean verifyOtp(String email, String otp) {
        OtpSessionDto session = otpRedisTemplate.opsForValue().get("otp-session:" + email);
        if (session == null) return false;

        boolean match = hashOtp(otp).equals(session.getHashedOtp());
        if (match) {
            session.setValid(true);
            otpRedisTemplate.opsForValue().set("otp-session:" + email, session, Duration.ofMinutes(12));
        }
        return match;
    }

    public boolean isEmailVerified(String email) {
        OtpSessionDto session = otpRedisTemplate.opsForValue().get("otp-session:" + email);
        return session != null && session.isValid();
    }

    public void deleteOtp(String email) {
        otpRedisTemplate.delete("otp-session:" + email);
    }



    private static String hashOtp(String otp) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(otp.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encoded) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing OTP", e);
        }
    }

}
