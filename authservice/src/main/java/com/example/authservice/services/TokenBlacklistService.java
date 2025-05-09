package com.example.authservice.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {
    private final RedisTemplate<String, String> redisTemplate;
    
    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    public void blacklistToken(String token, long ttl) {
        redisTemplate.opsForValue().set(token, "blacklisted", ttl, TimeUnit.MILLISECONDS);
    }
    
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}