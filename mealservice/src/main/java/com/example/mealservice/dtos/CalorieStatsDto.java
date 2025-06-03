package com.example.mealservice.dtos;

import java.time.LocalDate;

public class CalorieStatsDto {
    private String userId;
    private LocalDate date;
    private Integer totalCalories;

    public CalorieStatsDto() {}

    public CalorieStatsDto(String userId, LocalDate date, Integer totalCalories) {
        this.userId = userId;
        this.date = date;
        this.totalCalories = totalCalories;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getTotalCalories() { return totalCalories; }
    public void setTotalCalories(Integer totalCalories) { this.totalCalories = totalCalories; }
} 