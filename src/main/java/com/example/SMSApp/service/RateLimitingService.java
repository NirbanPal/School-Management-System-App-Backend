package com.example.SMSApp.service;


public interface RateLimitingService {

    boolean isAllowedIp(String ip);

}