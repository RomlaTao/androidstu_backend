package com.example.mealservice.entities;

import com.example.mealservice.entities.Meal;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "foods")
public class Food {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @Min(0)
    private Integer carb;
    
    @Min(0)
    private Integer protein;
    
    @Min(0)
    private Integer lipid;
    
    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;
    
    public Food() {
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
    
    public Meal getMeal() {
        return meal;
    }
    
    public void setMeal(Meal meal) {
        this.meal = meal;
    }
} 