package com.example.workoutservice.services;

import com.example.workoutservice.clients.UserClient;

/**
 * Service để tích hợp với UserService
 * Cung cấp thông tin user cần thiết cho calorie calculation
 */
public interface UserIntegrationService {
    
    /**
     * Lấy weight của user để tính calories
     * @param userId ID của user
     * @return Weight của user (kg)
     */
    Double getUserWeight(Long userId);
    
    /**
     * Cache user weight để tránh gọi UserService quá nhiều lần
     * @param userId ID của user  
     * @return Weight của user (kg) từ cache hoặc service call
     */
    Double getCachedUserWeight(Long userId);
    
    /**
     * Clear cache cho specific user (khi user update weight)
     * @param userId ID của user
     */
    void clearUserWeightCache(Long userId);
    
    /**
     * Validate user weight
     * @param weight Weight value
     * @return true nếu weight hợp lệ
     */
    boolean isValidWeight(Double weight);
} 