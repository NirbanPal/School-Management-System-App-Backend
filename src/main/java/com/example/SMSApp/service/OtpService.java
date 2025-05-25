package com.example.SMSApp.service;

public interface OtpService {

    boolean canSendOtp(String email);

    void storeOtp(String email, String rawOtp);

    boolean verifyOtp(String email, String otp);

    boolean isEmailVerified(String email);

    void deleteOtp(String email);

}
