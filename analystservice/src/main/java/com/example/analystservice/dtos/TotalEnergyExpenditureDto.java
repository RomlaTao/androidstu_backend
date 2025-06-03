package com.example.analystservice.dtos;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO cho tổng năng lượng tiêu thụ hàng ngày (BMR + Calories từ workout)
 */
public class TotalEnergyExpenditureDto {
    private String userId;
    private LocalDate date;
    private Double bmr; // Base Metabolic Rate
    private Integer workoutCaloriesBurned; // Calories đốt cháy từ workout
    private Double totalEnergyExpenditure; // Tổng năng lượng tiêu thụ (BMR + workout)
    private Integer workoutCount;
    private Integer totalWorkoutDuration;
    private Double caloriesPerMinuteWorkout; // Cường độ tập luyện (calories/phút)
    
    // Breakdown chi tiết calories từ workout
    private Integer cardioCalories;
    private Integer strengthCalories;
    private Integer yogaCalories;
    private Integer otherCalories;
    
    // Chi tiết các workout trong ngày
    private List<WorkoutSummaryDto> workoutSummary;

    public TotalEnergyExpenditureDto() {}

    public TotalEnergyExpenditureDto(String userId, LocalDate date, Double bmr, Integer workoutCaloriesBurned) {
        this.userId = userId;
        this.date = date;
        this.bmr = bmr;
        this.workoutCaloriesBurned = workoutCaloriesBurned;
        this.totalEnergyExpenditure = bmr + workoutCaloriesBurned;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getBmr() { return bmr; }
    public void setBmr(Double bmr) { 
        this.bmr = bmr;
        updateTotalEnergyExpenditure();
    }

    public Integer getWorkoutCaloriesBurned() { return workoutCaloriesBurned; }
    public void setWorkoutCaloriesBurned(Integer workoutCaloriesBurned) { 
        this.workoutCaloriesBurned = workoutCaloriesBurned;
        updateTotalEnergyExpenditure();
    }

    public Double getTotalEnergyExpenditure() { return totalEnergyExpenditure; }
    public void setTotalEnergyExpenditure(Double totalEnergyExpenditure) { this.totalEnergyExpenditure = totalEnergyExpenditure; }

    public Integer getWorkoutCount() { return workoutCount; }
    public void setWorkoutCount(Integer workoutCount) { this.workoutCount = workoutCount; }

    public Integer getTotalWorkoutDuration() { return totalWorkoutDuration; }
    public void setTotalWorkoutDuration(Integer totalWorkoutDuration) { 
        this.totalWorkoutDuration = totalWorkoutDuration;
        updateCaloriesPerMinute();
    }

    public Double getCaloriesPerMinuteWorkout() { return caloriesPerMinuteWorkout; }
    public void setCaloriesPerMinuteWorkout(Double caloriesPerMinuteWorkout) { this.caloriesPerMinuteWorkout = caloriesPerMinuteWorkout; }

    public Integer getCardioCalories() { return cardioCalories; }
    public void setCardioCalories(Integer cardioCalories) { this.cardioCalories = cardioCalories; }

    public Integer getStrengthCalories() { return strengthCalories; }
    public void setStrengthCalories(Integer strengthCalories) { this.strengthCalories = strengthCalories; }

    public Integer getYogaCalories() { return yogaCalories; }
    public void setYogaCalories(Integer yogaCalories) { this.yogaCalories = yogaCalories; }

    public Integer getOtherCalories() { return otherCalories; }
    public void setOtherCalories(Integer otherCalories) { this.otherCalories = otherCalories; }

    public List<WorkoutSummaryDto> getWorkoutSummary() { return workoutSummary; }
    public void setWorkoutSummary(List<WorkoutSummaryDto> workoutSummary) { this.workoutSummary = workoutSummary; }

    // Helper methods
    private void updateTotalEnergyExpenditure() {
        if (bmr != null && workoutCaloriesBurned != null) {
            this.totalEnergyExpenditure = bmr + workoutCaloriesBurned;
        }
    }

    private void updateCaloriesPerMinute() {
        if (workoutCaloriesBurned != null && totalWorkoutDuration != null && totalWorkoutDuration > 0) {
            this.caloriesPerMinuteWorkout = (double) workoutCaloriesBurned / totalWorkoutDuration;
        }
    }

    public static class WorkoutSummaryDto {
        private String workoutName;
        private String workoutType;
        private Integer caloriesBurned;
        private Integer durationMinutes;
        private String scheduledTime;

        public WorkoutSummaryDto() {}

        public WorkoutSummaryDto(String workoutName, String workoutType, Integer caloriesBurned, 
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