package com.example.mealservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
    
    @NotNull
    private Integer carb;
    
    @NotNull
    private Integer protein;
    
    @NotNull
    private Integer lipid;
    
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
    
    public Set<Food> getFoods() {
        return foods;
    }
    
    public void setFoods(Set<Food> foods) {
        this.foods = foods;
    }
    
    public void addFood(Food food) {
        foods.add(food);
        food.setMeal(this);
    }
    
    public void removeFood(Food food) {
        foods.remove(food);
        food.setMeal(null);
    }
}