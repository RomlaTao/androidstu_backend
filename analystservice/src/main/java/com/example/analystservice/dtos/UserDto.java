package com.example.analystservice.dtos;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class UserDto {
    private String id;
    private String fullName;
    private String email;
    private Date birthDate;
    private String gender;
    private Double height; // cm
    private Double weight; // kg
    private String initialActivityLevel;
    private Date activityLevelSetAt;
    private Double userInputTdee; // TDEE nhập bởi người dùng
    private Date tdeeUpdatedAt;
    private Date createdAt;
    private Date updatedAt;

    // Constructors
    public UserDto() {}

    public UserDto(String id, String fullName, String email, Date birthDate, String gender, 
                   Double height, Double weight, Date createdAt, Date updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Calculate age from birthDate
    public Integer getAge() {
        if (birthDate == null) {
            return null;
        }
        LocalDate birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(birthLocalDate, LocalDate.now()).getYears();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getInitialActivityLevel() { return initialActivityLevel; }
    public void setInitialActivityLevel(String initialActivityLevel) { this.initialActivityLevel = initialActivityLevel; }

    public Date getActivityLevelSetAt() { return activityLevelSetAt; }
    public void setActivityLevelSetAt(Date activityLevelSetAt) { this.activityLevelSetAt = activityLevelSetAt; }

    public Double getUserInputTdee() { return userInputTdee; }
    public void setUserInputTdee(Double userInputTdee) { this.userInputTdee = userInputTdee; }

    public Date getTdeeUpdatedAt() { return tdeeUpdatedAt; }
    public void setTdeeUpdatedAt(Date tdeeUpdatedAt) { this.tdeeUpdatedAt = tdeeUpdatedAt; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}