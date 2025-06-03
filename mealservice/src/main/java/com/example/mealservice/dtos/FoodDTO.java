package com.example.mealservice.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class FoodDTO {
    
    private Long id;
    
    @NotBlank(message = "Food name is required")
    private String name;
    
    private String description;
    
    @Min(value = 0, message = "Calories must be a non-negative number")
    private Integer calories;
    
    public FoodDTO() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getCalories() {
        return calories;
    }
    
    public void setCalories(Integer calories) {
        this.calories = calories;
    }
} 