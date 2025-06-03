package com.example.mealservice.dtos;

import com.example.mealservice.enums.MealType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class MealDTO {

    private Long id;

    @NotBlank(message = "Meal name is required")
    private String name;

    private String description;

    @Min(value = 0, message = "Calories must be a non-negative number")
    private Integer calories;

    @NotNull(message = "Meal type is required")
    private MealType type;

    private List<FoodDTO> foods;

    public MealDTO() {
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

    public MealType getType() {
        return type;
    }

    public void setType(MealType type) {
        this.type = type;
    }

    public List<FoodDTO> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodDTO> foods) {
        this.foods = foods;
    }
} 