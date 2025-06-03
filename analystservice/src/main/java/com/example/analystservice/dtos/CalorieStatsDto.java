package com.example.analystservice.dtos;

import java.time.LocalDate;
import java.util.List;

public class CalorieStatsDto {
    private String userId;
    private LocalDate date;
    private Integer totalCalories;
    private Integer breakfastCalories;
    private Integer lunchCalories;
    private Integer dinnerCalories;
    private Integer snackCalories;
    private Integer mealCount;
    private List<MealDetailDto> mealDetails;

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

    public Integer getBreakfastCalories() { return breakfastCalories; }
    public void setBreakfastCalories(Integer breakfastCalories) { this.breakfastCalories = breakfastCalories; }

    public Integer getLunchCalories() { return lunchCalories; }
    public void setLunchCalories(Integer lunchCalories) { this.lunchCalories = lunchCalories; }

    public Integer getDinnerCalories() { return dinnerCalories; }
    public void setDinnerCalories(Integer dinnerCalories) { this.dinnerCalories = dinnerCalories; }

    public Integer getSnackCalories() { return snackCalories; }
    public void setSnackCalories(Integer snackCalories) { this.snackCalories = snackCalories; }

    public Integer getMealCount() { return mealCount; }
    public void setMealCount(Integer mealCount) { this.mealCount = mealCount; }

    public List<MealDetailDto> getMealDetails() { return mealDetails; }
    public void setMealDetails(List<MealDetailDto> mealDetails) { this.mealDetails = mealDetails; }

    public static class MealDetailDto {
        private String mealName;
        private String mealType;
        private Integer calories;
        private String scheduledTime;

        public MealDetailDto() {}

        public MealDetailDto(String mealName, String mealType, Integer calories, String scheduledTime) {
            this.mealName = mealName;
            this.mealType = mealType;
            this.calories = calories;
            this.scheduledTime = scheduledTime;
        }

        // Getters and Setters
        public String getMealName() { return mealName; }
        public void setMealName(String mealName) { this.mealName = mealName; }

        public String getMealType() { return mealType; }
        public void setMealType(String mealType) { this.mealType = mealType; }

        public Integer getCalories() { return calories; }
        public void setCalories(Integer calories) { this.calories = calories; }

        public String getScheduledTime() { return scheduledTime; }
        public void setScheduledTime(String scheduledTime) { this.scheduledTime = scheduledTime; }
    }
} 