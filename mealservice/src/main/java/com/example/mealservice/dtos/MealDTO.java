package com.example.mealservice.dtos;

import com.example.mealservice.entities.MealType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class MealDTO {

    private Long id;

    @NotBlank(message = "Meal name is required")
    private String name;

    private String description;

    @NotNull(message = "Carb is required")
    @Min(value = 1, message = "Carb must be a non-negative number")
    private Integer carb;

    @NotNull(message = "Protein is required")
    @Min(value = 0, message = "Protein must be a non-negative number")
    private Integer protein;

    @NotNull(message = "Lipid is required")
    @Min(value = 0, message = "Lipid must be a non-negative number")
    private Integer lipid;

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

    public Integer getCarb() {
        return carb;
    }

    public void setCarb(Integer carb) {
        this.carb = carb;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getLipid() {
        return lipid;
    }

    public void setLipid(Integer lipid) {
        this.lipid = lipid;
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