package com.example.workoutservice.services.impl;

import com.example.workoutservice.clients.UserClient;
import com.example.workoutservice.services.UserIntegrationService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Implementation của UserIntegrationService với caching mechanism
 */
@Service
public class UserIntegrationServiceImpl implements UserIntegrationService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserIntegrationServiceImpl.class);
    private static final Double DEFAULT_WEIGHT_KG = 70.0;
    private static final Double MIN_WEIGHT_KG = 30.0;
    private static final Double MAX_WEIGHT_KG = 300.0;
    
    // Cache để lưu user weight, tránh gọi UserService quá nhiều
    private final ConcurrentHashMap<Long, CacheEntry> weightCache = new ConcurrentHashMap<>();
    private final long CACHE_EXPIRY_MINUTES = 10; // Cache expires after 10 minutes
    
    private final UserClient userClient;
    private final ScheduledExecutorService scheduler;
    
    public UserIntegrationServiceImpl(UserClient userClient) {
        this.userClient = userClient;
        this.scheduler = Executors.newScheduledThreadPool(1);
        
        // Schedule cache cleanup every 5 minutes
        scheduler.scheduleAtFixedRate(this::cleanupExpiredCache, 5, 5, TimeUnit.MINUTES);
    }
    
    @Override
    public Double getUserWeight(Long userId) {
        if (userId == null) {
            logger.warn("UserId is null, returning default weight: {} kg", DEFAULT_WEIGHT_KG);
            return DEFAULT_WEIGHT_KG;
        }
        
        try {
            Double weight = userClient.getUserWeight(userId);
            
            if (isValidWeight(weight)) {
                logger.debug("Retrieved valid weight: {} kg for userId: {}", weight, userId);
                return weight;
            } else {
                logger.warn("Invalid weight: {} for userId: {}, using default: {} kg", 
                           weight, userId, DEFAULT_WEIGHT_KG);
                return DEFAULT_WEIGHT_KG;
            }
            
        } catch (Exception e) {
            logger.error("Error getting user weight for userId: {}, using default: {} kg. Error: {}", 
                        userId, DEFAULT_WEIGHT_KG, e.getMessage());
            return DEFAULT_WEIGHT_KG;
        }
    }
    
    @Override
    public Double getCachedUserWeight(Long userId) {
        if (userId == null) {
            return DEFAULT_WEIGHT_KG;
        }
        
        // Check cache first
        CacheEntry cacheEntry = weightCache.get(userId);
        if (cacheEntry != null && !cacheEntry.isExpired()) {
            logger.debug("Retrieved cached weight: {} kg for userId: {}", cacheEntry.getWeight(), userId);
            return cacheEntry.getWeight();
        }
        
        // Cache miss or expired, fetch from service
        Double weight = getUserWeight(userId);
        
        // Cache the result
        weightCache.put(userId, new CacheEntry(weight, System.currentTimeMillis()));
        
        return weight;
    }
    
    @Override
    public void clearUserWeightCache(Long userId) {
        if (userId != null) {
            weightCache.remove(userId);
            logger.debug("Cleared weight cache for userId: {}", userId);
        }
    }
    
    @Override
    public boolean isValidWeight(Double weight) {
        return weight != null && 
               weight >= MIN_WEIGHT_KG && 
               weight <= MAX_WEIGHT_KG;
    }
    
    /**
     * Cleanup expired cache entries
     */
    private void cleanupExpiredCache() {
        long currentTime = System.currentTimeMillis();
        long expiryTime = CACHE_EXPIRY_MINUTES * 60 * 1000; // Convert to milliseconds
        
        weightCache.entrySet().removeIf(entry -> {
            boolean expired = (currentTime - entry.getValue().getTimestamp()) > expiryTime;
            if (expired) {
                logger.debug("Removed expired cache entry for userId: {}", entry.getKey());
            }
            return expired;
        });
    }
    
    /**
     * Cache entry để lưu weight và timestamp
     */
    private static class CacheEntry {
        private final Double weight;
        private final long timestamp;
        
        public CacheEntry(Double weight, long timestamp) {
            this.weight = weight;
            this.timestamp = timestamp;
        }
        
        public Double getWeight() {
            return weight;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
        
        public boolean isExpired() {
            long currentTime = System.currentTimeMillis();
            long expiryTime = 10 * 60 * 1000; // 10 minutes in milliseconds
            return (currentTime - timestamp) > expiryTime;
        }
    }
} 