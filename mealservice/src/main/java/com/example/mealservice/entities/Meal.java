package com.example.mealservice.entities;

import com.example.mealservice.enums.MealType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meals")
public class Meal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @Min(value = 0, message = "Calories must be a non-negative number")
    private Integer calories;
    
    @Enumerated(EnumType.STRING)
    private MealType type;
    
    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Food> foods = new HashSet<>();
    
    public Meal() {
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
    
    public Set<Food> getFoods() {
        return foods;
    }
    
    public void setFoods(Set<Food> foods) {
        this.foods = foods;
    }
    
    public void addFood(Food food) {
        foods.add(food);
        food.setMeal(this);
        updateCaloriesFromFoods();
    }
    
    public void removeFood(Food food) {
        foods.remove(food);
        food.setMeal(null);
        updateCaloriesFromFoods();
    }
    
    /**
     * Tự động cập nhật calories của meal dựa trên tổng calories của foods
     */
    public void updateCaloriesFromFoods() {
        this.calories = foods.stream()
                .mapToInt(food -> food.getCalories() != null ? food.getCalories() : 0)
                .sum();
    }
    
    /**
     * Validate rằng calories của meal phải bằng tổng calories của foods
     */
    public boolean isCaloriesConsistent() {
        int calculatedCalories = foods.stream()
                .mapToInt(food -> food.getCalories() != null ? food.getCalories() : 0)
                .sum();
        return this.calories != null && this.calories.equals(calculatedCalories);
    }
    
    @PrePersist
    @PreUpdate
    public void validateAndUpdateCalories() {
        updateCaloriesFromFoods();
    }
}