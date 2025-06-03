package com.example.analystservice.dtos;

import java.time.LocalDate;
import java.util.List;

public class CalorieBurnStatsDto {
    private String userId;
    private LocalDate date;
    private Integer totalCaloriesBurned;
    private Integer cardioCalories;
    private Integer strengthCalories;
    private Integer yogaCalories;
    private Integer otherCalories;
    private Integer workoutCount;
    private Integer totalDurationMinutes;
    private List<WorkoutDetailDto> workoutDetails;

    public CalorieBurnStatsDto() {}

    public CalorieBurnStatsDto(String userId, LocalDate date, Integer totalCaloriesBurned) {
        this.userId = userId;
        this.date = date;
        this.totalCaloriesBurned = totalCaloriesBurned;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getTotalCaloriesBurned() { return totalCaloriesBurned; }
    public void setTotalCaloriesBurned(Integer totalCaloriesBurned) { this.totalCaloriesBurned = totalCaloriesBurned; }

    public Integer getCardioCalories() { return cardioCalories; }
    public void setCardioCalories(Integer cardioCalories) { this.cardioCalories = cardioCalories; }

    public Integer getStrengthCalories() { return strengthCalories; }
    public void setStrengthCalories(Integer strengthCalories) { this.strengthCalories = strengthCalories; }

    public Integer getYogaCalories() { return yogaCalories; }
    public void setYogaCalories(Integer yogaCalories) { this.yogaCalories = yogaCalories; }

    public Integer getOtherCalories() { return otherCalories; }
    public void setOtherCalories(Integer otherCalories) { this.otherCalories = otherCalories; }

    public Integer getWorkoutCount() { return workoutCount; }
    public void setWorkoutCount(Integer workoutCount) { this.workoutCount = workoutCount; }

    public Integer getTotalDurationMinutes() { return totalDurationMinutes; }
    public void setTotalDurationMinutes(Integer totalDurationMinutes) { this.totalDurationMinutes = totalDurationMinutes; }

    public List<WorkoutDetailDto> getWorkoutDetails() { return workoutDetails; }
    public void setWorkoutDetails(List<WorkoutDetailDto> workoutDetails) { this.workoutDetails = workoutDetails; }

    public static class WorkoutDetailDto {
        private String workoutName;
        private String workoutType;
        private Integer caloriesBurned;
        private Integer durationMinutes;
        private String scheduledTime;

        public WorkoutDetailDto() {}

        public WorkoutDetailDto(String workoutName, String workoutType, Integer caloriesBurned, 
                               Integer durationMinutes, String scheduledTime) {
            this.workoutName = workoutName;
            this.workoutType = workoutType;
            this.caloriesBurned = caloriesBurned;
            this.durationMinutes = durationMinutes;
            this.scheduledTime = scheduledTime;
        }

        // Getters and Setters
        public String getWorkoutName() { return workoutName; }
        public void setWorkoutName(String workoutName) { this.workoutName = workoutName; }

        public String getWorkoutType() { return workoutType; }
        public void setWorkoutType(String workoutType) { this.workoutType = workoutType; }

        public Integer getCaloriesBurned() { return caloriesBurned; }
        public void setCaloriesBurned(Integer caloriesBurned) { this.caloriesBurned = caloriesBurned; }

        public Integer getDurationMinutes() { return durationMinutes; }
        public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

        public String getScheduledTime() { return scheduledTime; }
        public void setScheduledTime(String scheduledTime) { this.scheduledTime = scheduledTime; }
    }
} 