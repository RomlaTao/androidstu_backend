package com.example.analystservice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class
HealthCalculationRequest {
    
    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private Double weight;
    
    @NotNull(message = "Height is required")
    @Positive(message = "Height must be positive")
    private Double height;
    
    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 150, message = "Age must be less than 150")
    private Integer age;
    
    @NotNull(message = "Gender is required")
    private String gender;
    
    private String activityLevel;

    // Constructors
    public HealthCalculationRequest() {}

    public HealthCalculationRequest(Double weight, Double height, Integer age, String gender, String activityLevel) {
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.activityLevel = activityLevel;
    }

    // Getters and Setters
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    @Override
    public String toString() {
        return "HealthCalculationRequest{" +
                "weight=" + weight +
                ", height=" + height +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", activityLevel='" + activityLevel + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        HealthCalculationRequest that = (HealthCalculationRequest) o;
        
        if (!weight.equals(that.weight)) return false;
        if (!height.equals(that.height)) return false;
        if (!age.equals(that.age)) return false;
        if (!gender.equals(that.gender)) return false;
        return activityLevel != null ? activityLevel.equals(that.activityLevel) : that.activityLevel == null;
    }

    @Override
    public int hashCode() {
        int result = weight.hashCode();
        result = 31 * result + height.hashCode();
        result = 31 * result + age.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + (activityLevel != null ? activityLevel.hashCode() : 0);
        return result;
    }
} 