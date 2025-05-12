package com.example.SMSApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimitingService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final int MAX_REQUESTS_PER_IP = 10;
    private static final Duration IP_RATE_LIMIT_DURATION = Duration.ofHours(1);

    public boolean isAllowedIp(String ip) {
        String key = "rate-limit:" + ip;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String countStr = ops.get(key);
        int count = countStr != null ? Integer.parseInt(countStr) : 0;
        if (count >= MAX_REQUESTS_PER_IP) return false;

        ops.increment(key);
        if (count == 0) redisTemplate.expire(key, IP_RATE_LIMIT_DURATION);
        return true;
    }
}

