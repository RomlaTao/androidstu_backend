package com.example.mealservice.services;

import com.example.mealservice.dtos.MealDTO;
import com.example.mealservice.enums.MealType;

import java.util.List;

public interface MealService {
    
    MealDTO createMeal(MealDTO MealDTO);
    
    MealDTO getMealById(Long id);
    
    List<MealDTO> getAllMeals();
    
    List<MealDTO> getMealsByType(MealType type);

    List<MealDTO> searchMealsByName(String name);

    MealDTO updateMeal(Long id, MealDTO workoutDTO);
    
    void deleteMeal(Long id);
} 