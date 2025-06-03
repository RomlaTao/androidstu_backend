package com.example.userservice.dtos;

import com.example.userservice.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.util.Date;

public class UserDto {
    private String id;

    private String fullName;

    private String email;

    private String password;
    
    private User.Gender gender;
    
    @Past(message = "Birth date must be in the past")
    private Date birthDate;
    
    @DecimalMin(value = "1.0", message = "Weight must be at least 1 kg")
    @DecimalMax(value = "500.0", message = "Weight must be less than 500 kg")
    private Double weight;
    
    @DecimalMin(value = "1.0", message = "Height must be at least 1 cm")
    @DecimalMax(value = "300.0", message = "Height must be less than 300 cm")
    private Double height;
    
    private User.InitialActivityLevel initialActivityLevel;

    private Date activityLevelSetAt;

    private Date createdAt;
    private Date updatedAt;

    // Constructors
    public UserDto() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User.Gender getGender() {
        return gender;
    }

    public void setGender(User.Gender gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public User.InitialActivityLevel getInitialActivityLevel() {
        return initialActivityLevel;
    }

    public void setInitialActivityLevel(User.InitialActivityLevel initialActivityLevel) {
        this.initialActivityLevel = initialActivityLevel;
    }

    public Date getActivityLevelSetAt() {
        return activityLevelSetAt;
    }

    public void setActivityLevelSetAt(Date activityLevelSetAt) {
        this.activityLevelSetAt = activityLevelSetAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
} 