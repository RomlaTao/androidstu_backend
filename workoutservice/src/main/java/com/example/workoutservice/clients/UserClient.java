package com.example.workoutservice.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client để gọi UserService lấy thông tin user
 */
@Component
public class UserClient {
    
    private static final Logger logger = LoggerFactory.getLogger(UserClient.class);
    private static final Double DEFAULT_WEIGHT_KG = 70.0; // Fallback weight
    
    private final RestTemplate restTemplate;
    private final String userServiceUrl;
    
    public UserClient(RestTemplate restTemplate, 
                     @Value("${userservice.url:http://localhost:8001}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }
    
    /**
     * Lấy weight của user từ UserService
     * @param userId ID của user
     * @return Weight của user (kg), hoặc default weight nếu không lấy được
     */
    public Double getUserWeight(Long userId) {
        try {
            String url = userServiceUrl + "/users/" + userId;
            logger.debug("Calling UserService: {}", url);
            
            UserResponse userResponse = restTemplate.getForObject(url, UserResponse.class);
            
            if (userResponse != null && userResponse.getWeight() != null && userResponse.getWeight() > 0) {
                logger.debug("Retrieved user weight: {} kg for userId: {}", userResponse.getWeight(), userId);
                return userResponse.getWeight();
            } else {
                logger.warn("User weight not available for userId: {}, using default weight: {} kg", 
                           userId, DEFAULT_WEIGHT_KG);
                return DEFAULT_WEIGHT_KG;
            }
            
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("User not found with ID: {}, using default weight: {} kg", userId, DEFAULT_WEIGHT_KG);
            return DEFAULT_WEIGHT_KG;
        } catch (Exception e) {
            logger.error("Error calling UserService for userId: {}, using default weight: {} kg. Error: {}", 
                        userId, DEFAULT_WEIGHT_KG, e.getMessage());
            return DEFAULT_WEIGHT_KG;
        }
    }
    
    /**
     * Lấy thông tin đầy đủ của user (nếu cần trong tương lai)
     */
    public UserResponse getUserInfo(Long userId) {
        try {
            String url = userServiceUrl + "/users/" + userId;
            return restTemplate.getForObject(url, UserResponse.class);
        } catch (Exception e) {
            logger.error("Error getting user info for userId: {}, Error: {}", userId, e.getMessage());
            return null;
        }
    }
    
    /**
     * DTO để receive response từ UserService
     */
    public static class UserResponse {
        private Integer id;
        private String fullName;
        private String email;
        private Double weight; // kg
        private Double height; // cm
        private String gender;
        
        // Constructors
        public UserResponse() {}
        
        // Getters and Setters
        public Integer getId() {
            return id;
        }
        
        public void setId(Integer id) {
            this.id = id;
        }
        
        public String getFullName() {
            return fullName;
        }
        
        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public Double getWeight() {
            return weight;
        }
        
        public void setWeight(Double weight) {
            this.weight = weight;
        }
        
        public Double getHeight() {
            return height;
        }
        
        public void setHeight(Double height) {
            this.height = height;
        }
        
        public String getGender() {
            return gender;
        }
        
        public void setGender(String gender) {
            this.gender = gender;
        }
    }
} 