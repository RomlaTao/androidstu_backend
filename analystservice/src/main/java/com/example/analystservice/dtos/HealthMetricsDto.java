package com.example.analystservice.dtos;

import java.time.LocalDateTime;

public class HealthMetricsDto {
    private String userId;
    private String fullName;
    private Double height;
    private Double weight;
    private Integer age;
    private String gender;
    private Double bmi;
    private String bmiCategory;
    private Double bmr;
    private String bmrFormula;
    private LocalDateTime calculatedAt;

    // Constructors
    public HealthMetricsDto() {}

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;
        private String fullName;
        private Double height;
        private Double weight;
        private Integer age;
        private String gender;
        private Double bmi;
        private String bmiCategory;
        private Double bmr;
        private String bmrFormula;
        private LocalDateTime calculatedAt;

        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder height(Double height) { this.height = height; return this; }
        public Builder weight(Double weight) { this.weight = weight; return this; }
        public Builder age(Integer age) { this.age = age; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder bmi(Double bmi) { this.bmi = bmi; return this; }
        public Builder bmiCategory(String bmiCategory) { this.bmiCategory = bmiCategory; return this; }
        public Builder bmr(Double bmr) { this.bmr = bmr; return this; }
        public Builder bmrFormula(String bmrFormula) { this.bmrFormula = bmrFormula; return this; }
        public Builder calculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; return this; }

        public HealthMetricsDto build() {
            HealthMetricsDto dto = new HealthMetricsDto();
            dto.userId = this.userId;
            dto.fullName = this.fullName;
            dto.height = this.height;
            dto.weight = this.weight;
            dto.age = this.age;
            dto.gender = this.gender;
            dto.bmi = this.bmi;
            dto.bmiCategory = this.bmiCategory;
            dto.bmr = this.bmr;
            dto.bmrFormula = this.bmrFormula;
            dto.calculatedAt = this.calculatedAt;
            return dto;
        }
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Double getBmi() { return bmi; }
    public void setBmi(Double bmi) { this.bmi = bmi; }

    public String getBmiCategory() { return bmiCategory; }
    public void setBmiCategory(String bmiCategory) { this.bmiCategory = bmiCategory; }

    public Double getBmr() { return bmr; }
    public void setBmr(Double bmr) { this.bmr = bmr; }

    public String getBmrFormula() { return bmrFormula; }
    public void setBmrFormula(String bmrFormula) { this.bmrFormula = bmrFormula; }

    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
}