package com.example.authservice.dtos;

import java.util.Date;

public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;
    private String gender;
    private Date birthDate;
    private Double weight;
    private Double height;

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }
    
    public String getGender() {
        return gender;
    }
    
    public Date getBirthDate() {
        return birthDate;
    }
    
    public Double getWeight() {
        return weight;
    }
    
    public Double getHeight() {
        return height;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public void setHeight(Double height) {
        this.height = height;
    }
}