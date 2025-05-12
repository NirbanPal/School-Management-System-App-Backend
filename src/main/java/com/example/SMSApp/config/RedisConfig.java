package com.example.SMSApp.config;


import com.example.SMSApp.dto.OtpSessionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    /**
     * RedisTemplate for storing OtpSessionDto objects as JSON.
     */

    @Bean
    public RedisTemplate<String, OtpSessionDto> otpRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, OtpSessionDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());

        // Custom ObjectMapper to handle LocalDateTime serialization
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule

        // Instead of setObjectMapper, we use the constructor of Jackson2JsonRedisSerializer
        Jackson2JsonRedisSerializer<OtpSessionDto> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, OtpSessionDto.class);

        // Set the serializer for values
        template.setValueSerializer(serializer);

        return template;
    }
}

