package com.example.userservice.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceClient.class);

    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public AuthServiceClient(RestTemplate restTemplate,
                           @Value("${services.authservice.url}") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    /**
     * Gọi AuthService để đánh dấu user đã hoàn thành khởi tạo thông tin
     * @param userId ID của user
     */
    public void markUserInfoInitialized(String userId) {
        try {
            String url = authServiceUrl + "/auth/internal/mark-user-info-initialized/" + userId;
            restTemplate.put(url, null);
            logger.info("Successfully marked user {} as info initialized", userId);
        } catch (Exception e) {
            logger.warn("Failed to mark user {} as info initialized: {}", userId, e.getMessage());
        }
    }
} 