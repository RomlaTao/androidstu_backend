package com.example.analystservice.dtos;

import java.time.LocalDate;

public class ActivityAnalysisDto {
    private String userId;
    private LocalDate analysisStartDate;
    private LocalDate analysisEndDate;
    private Integer totalWorkoutDays;
    private Double calculatedActivityFactor;
    private String activityLevelDescription;

    // Constructors
    public ActivityAnalysisDto() {}

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;
        private LocalDate analysisStartDate;
        private LocalDate analysisEndDate;
        private Integer totalWorkoutDays;
        private Double calculatedActivityFactor;
        private String activityLevelDescription;

        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder analysisStartDate(LocalDate analysisStartDate) { this.analysisStartDate = analysisStartDate; return this; }
        public Builder analysisEndDate(LocalDate analysisEndDate) { this.analysisEndDate = analysisEndDate; return this; }
        public Builder totalWorkoutDays(Integer totalWorkoutDays) { this.totalWorkoutDays = totalWorkoutDays; return this; }
        public Builder calculatedActivityFactor(Double calculatedActivityFactor) { this.calculatedActivityFactor = calculatedActivityFactor; return this; }
        public Builder activityLevelDescription(String activityLevelDescription) { this.activityLevelDescription = activityLevelDescription; return this; }

        public ActivityAnalysisDto build() {
            ActivityAnalysisDto dto = new ActivityAnalysisDto();
            dto.userId = this.userId;
            dto.analysisStartDate = this.analysisStartDate;
            dto.analysisEndDate = this.analysisEndDate;
            dto.totalWorkoutDays = this.totalWorkoutDays;
            dto.calculatedActivityFactor = this.calculatedActivityFactor;
            dto.activityLevelDescription = this.activityLevelDescription;
            return dto;
        }
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDate getAnalysisStartDate() { return analysisStartDate; }
    public void setAnalysisStartDate(LocalDate analysisStartDate) { this.analysisStartDate = analysisStartDate; }

    public LocalDate getAnalysisEndDate() { return analysisEndDate; }
    public void setAnalysisEndDate(LocalDate analysisEndDate) { this.analysisEndDate = analysisEndDate; }

    public Integer getTotalWorkoutDays() { return totalWorkoutDays; }
    public void setTotalWorkoutDays(Integer totalWorkoutDays) { this.totalWorkoutDays = totalWorkoutDays; }

    public Double getCalculatedActivityFactor() { return calculatedActivityFactor; }
    public void setCalculatedActivityFactor(Double calculatedActivityFactor) { this.calculatedActivityFactor = calculatedActivityFactor; }

    public String getActivityLevelDescription() { return activityLevelDescription; }
    public void setActivityLevelDescription(String activityLevelDescription) { this.activityLevelDescription = activityLevelDescription; }
}