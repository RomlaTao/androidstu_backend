package com.example.mealservice.services.impl;

import com.example.mealservice.entities.Meal;
import com.example.mealservice.services.CalorieValidationService;
import org.springframework.stereotype.Service;

@Service
public class CalorieValidationServiceImpl implements CalorieValidationService {
    
    @Override
    public boolean validateMealCalories(Meal meal) {
        if (meal == null || meal.getFoods() == null || meal.getFoods().isEmpty()) {
            return true; // No foods to validate against
        }
        
        int calculatedCalories = meal.getFoods().stream()
                .mapToInt(food -> food.getCalories() != null ? food.getCalories() : 0)
                .sum();
        
        return meal.getCalories() != null && meal.getCalories().equals(calculatedCalories);
    }
    
    @Override
    public void synchronizeMealCalories(Meal meal) {
        if (meal == null) {
            return;
        }
        
        if (meal.getFoods() != null && !meal.getFoods().isEmpty()) {
            int totalCalories = meal.getFoods().stream()
                    .mapToInt(food -> food.getCalories() != null ? food.getCalories() : 0)
                    .sum();
            meal.setCalories(totalCalories);
        } else if (meal.getCalories() == null) {
            meal.setCalories(0);
        }
    }
    
    @Override
    public void validateAndSynchronize(Meal meal) {
        synchronizeMealCalories(meal);
        
        if (!validateMealCalories(meal)) {
            throw new IllegalStateException(
                String.format("Meal calories (%d) do not match total food calories. Meal ID: %d", 
                    meal.getCalories(), meal.getId())
            );
        }
    }
} 