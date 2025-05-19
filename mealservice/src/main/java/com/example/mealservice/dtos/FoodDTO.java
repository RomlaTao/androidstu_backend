package com.example.mealservice.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class FoodDTO {
    
    private Long id;
    
    @NotBlank(message = "Food name is required")
    private String name;
    
    private String description;
    
    @Min(value = 0, message = "Carb must be a non-negative number")
    private Integer carb;
    
    @Min(value = 0, message = "Protein must be a non-negative number")
    private Integer protein;
    
    @Min(value = 0, message = "Lipid must be a non-negative number")
    private Integer lipid;
    
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
} 