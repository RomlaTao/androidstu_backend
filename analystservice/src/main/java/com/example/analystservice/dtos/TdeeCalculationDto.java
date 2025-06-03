package com.example.analystservice.dtos;

import java.time.LocalDateTime;

public class TdeeCalculationDto {
    private String userId;
    private String fullName;
    private Double bmr;
    private String activityLevel;
    private Double activityFactor;
    private Double tdee;
    private Double maintenanceCalories;
    private Double weightLossCalories;
    private Double weightGainCalories;
    private LocalDateTime calculatedAt;
    private String calculationMethod;
    private String methodDescription;

    // Constructors
    public TdeeCalculationDto() {}

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;
        private String fullName;
        private Double bmr;
        private String activityLevel;
        private Double activityFactor;
        private Double tdee;
        private Double maintenanceCalories;
        private Double weightLossCalories;
        private Double weightGainCalories;
        private LocalDateTime calculatedAt;
        private String calculationMethod;
        private String methodDescription;

        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder bmr(Double bmr) { this.bmr = bmr; return this; }
        public Builder activityLevel(String activityLevel) { this.activityLevel = activityLevel; return this; }
        public Builder activityFactor(Double activityFactor) { this.activityFactor = activityFactor; return this; }
        public Builder tdee(Double tdee) { this.tdee = tdee; return this; }
        public Builder maintenanceCalories(Double maintenanceCalories) { this.maintenanceCalories = maintenanceCalories; return this; }
        public Builder weightLossCalories(Double weightLossCalories) { this.weightLossCalories = weightLossCalories; return this; }
        public Builder weightGainCalories(Double weightGainCalories) { this.weightGainCalories = weightGainCalories; return this; }
        public Builder calculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; return this; }
        public Builder calculationMethod(String calculationMethod) { this.calculationMethod = calculationMethod; return this; }
        public Builder methodDescription(String methodDescription) { this.methodDescription = methodDescription; return this; }

        public TdeeCalculationDto build() {
            TdeeCalculationDto dto = new TdeeCalculationDto();
            dto.userId = this.userId;
            dto.fullName = this.fullName;
            dto.bmr = this.bmr;
            dto.activityLevel = this.activityLevel;
            dto.activityFactor = this.activityFactor;
            dto.tdee = this.tdee;
            dto.maintenanceCalories = this.maintenanceCalories;
            dto.weightLossCalories = this.weightLossCalories;
            dto.weightGainCalories = this.weightGainCalories;
            dto.calculatedAt = this.calculatedAt;
            dto.calculationMethod = this.calculationMethod;
            dto.methodDescription = this.methodDescription;
            return dto;
        }
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Double getBmr() { return bmr; }
    public void setBmr(Double bmr) { this.bmr = bmr; }

    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    public Double getActivityFactor() { return activityFactor; }
    public void setActivityFactor(Double activityFactor) { this.activityFactor = activityFactor; }

    public Double getTdee() { return tdee; }
    public void setTdee(Double tdee) { this.tdee = tdee; }

    public Double getMaintenanceCalories() { return maintenanceCalories; }
    public void setMaintenanceCalories(Double maintenanceCalories) { this.maintenanceCalories = maintenanceCalories; }

    public Double getWeightLossCalories() { return weightLossCalories; }
    public void setWeightLossCalories(Double weightLossCalories) { this.weightLossCalories = weightLossCalories; }

    public Double getWeightGainCalories() { return weightGainCalories; }
    public void setWeightGainCalories(Double weightGainCalories) { this.weightGainCalories = weightGainCalories; }

    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }

    public String getCalculationMethod() { return calculationMethod; }
    public void setCalculationMethod(String calculationMethod) { this.calculationMethod = calculationMethod; }

    public String getMethodDescription() { return methodDescription; }
    public void setMethodDescription(String methodDescription) { this.methodDescription = methodDescription; }
}