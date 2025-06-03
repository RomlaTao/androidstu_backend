package com.example.analystservice.dtos;

import java.time.LocalDate;
import java.util.List;

public class CalorieAnalysisDto {
    private String userId;
    private String fullName;
    private LocalDate analysisDate;
    
    // Calorie intake data
    private Integer caloriesConsumed;
    private Integer breakfastCalories;
    private Integer lunchCalories;
    private Integer dinnerCalories;
    private Integer snackCalories;
    private Integer mealCount;
    
    // TDEE data
    private Double userTdee;
    private String tdeeSource; // "calculated", "workout-based", "user-input"
    private Double bmr;
    private Double activityFactor;
    
    // Analysis results
    private Integer calorieDeficit; // Negative if surplus
    private Double weightChangeEstimate; // kg per week
    private String weightGoalStatus; // "deficit", "surplus", "maintenance"
    private String nutritionAdvice;
    
    // Insights
    private List<String> insights;
    private String overallAssessment;
    private Double calorieAccuracy; // How close to goal
    
    public CalorieAnalysisDto() {}

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getAnalysisDate() { return analysisDate; }
    public void setAnalysisDate(LocalDate analysisDate) { this.analysisDate = analysisDate; }

    public Integer getCaloriesConsumed() { return caloriesConsumed; }
    public void setCaloriesConsumed(Integer caloriesConsumed) { this.caloriesConsumed = caloriesConsumed; }

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

    public Double getUserTdee() { return userTdee; }
    public void setUserTdee(Double userTdee) { this.userTdee = userTdee; }

    public String getTdeeSource() { return tdeeSource; }
    public void setTdeeSource(String tdeeSource) { this.tdeeSource = tdeeSource; }

    public Double getBmr() { return bmr; }
    public void setBmr(Double bmr) { this.bmr = bmr; }

    public Double getActivityFactor() { return activityFactor; }
    public void setActivityFactor(Double activityFactor) { this.activityFactor = activityFactor; }

    public Integer getCalorieDeficit() { return calorieDeficit; }
    public void setCalorieDeficit(Integer calorieDeficit) { this.calorieDeficit = calorieDeficit; }

    public Double getWeightChangeEstimate() { return weightChangeEstimate; }
    public void setWeightChangeEstimate(Double weightChangeEstimate) { this.weightChangeEstimate = weightChangeEstimate; }

    public String getWeightGoalStatus() { return weightGoalStatus; }
    public void setWeightGoalStatus(String weightGoalStatus) { this.weightGoalStatus = weightGoalStatus; }

    public String getNutritionAdvice() { return nutritionAdvice; }
    public void setNutritionAdvice(String nutritionAdvice) { this.nutritionAdvice = nutritionAdvice; }

    public List<String> getInsights() { return insights; }
    public void setInsights(List<String> insights) { this.insights = insights; }

    public String getOverallAssessment() { return overallAssessment; }
    public void setOverallAssessment(String overallAssessment) { this.overallAssessment = overallAssessment; }

    public Double getCalorieAccuracy() { return calorieAccuracy; }
    public void setCalorieAccuracy(Double calorieAccuracy) { this.calorieAccuracy = calorieAccuracy; }
} 