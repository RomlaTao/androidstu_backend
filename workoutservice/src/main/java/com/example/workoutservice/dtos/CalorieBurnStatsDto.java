package com.example.workoutservice.dtos;

import java.time.LocalDate;

/**
 * Simplified DTO cho calorie burn statistics
 * Chỉ chứa 3 fields cần thiết để consistency với mealservice
 */
public class CalorieBurnStatsDto {
    private String userId;
    private LocalDate date;
    private Integer totalCaloriesBurned;

    public CalorieBurnStatsDto() {}

    public CalorieBurnStatsDto(String userId, LocalDate date, Integer totalCaloriesBurned) {
        this.userId = userId;
        this.date = date;
        this.totalCaloriesBurned = totalCaloriesBurned;
    }

    // Getters and Setters
    public String getUserId() { 
        return userId; 
    }
    
    public void setUserId(String userId) { 
        this.userId = userId; 
    }

    public LocalDate getDate() { 
        return date; 
    }
    
    public void setDate(LocalDate date) { 
        this.date = date; 
    }

    public Integer getTotalCaloriesBurned() { 
        return totalCaloriesBurned; 
    }
    
    public void setTotalCaloriesBurned(Integer totalCaloriesBurned) { 
        this.totalCaloriesBurned = totalCaloriesBurned; 
    }
} 