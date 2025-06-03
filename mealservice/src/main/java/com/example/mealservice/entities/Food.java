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
    private Integer calories;
    
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
    
    public Integer getCalories() {
        return calories;
    }
    
    public void setCalories(Integer calories) {
        this.calories = calories;
        // Tự động cập nhật calories của meal khi food calories thay đổi
        if (this.meal != null) {
            this.meal.updateCaloriesFromFoods();
        }
    }
    
    public Meal getMeal() {
        return meal;
    }
    
    public void setMeal(Meal meal) {
        this.meal = meal;
    }
    
    @PostPersist
    @PostUpdate
    @PostRemove
    public void updateMealCalories() {
        if (this.meal != null) {
            this.meal.updateCaloriesFromFoods();
        }
    }
} 