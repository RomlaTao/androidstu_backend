package com.example.apiservice.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {
    private final RedisTemplate<String, String> redisTemplate;
    
    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * Kiểm tra xem token có trong blacklist hay không
     * @param token JWT token cần kiểm tra
     * @return true nếu token đã bị blacklist, false nếu không
     */
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
} 