package com.example.mealservice.services;

import com.example.mealservice.entities.Meal;

/**
 * Service để validate và đồng bộ calories giữa Meal và Foods
 */
public interface CalorieValidationService {
    
    /**
     * Validate rằng calories của meal bằng tổng calories của foods
     */
    boolean validateMealCalories(Meal meal);
    
    /**
     * Tự động cập nhật calories của meal từ tổng calories của foods
     */
    void synchronizeMealCalories(Meal meal);
    
    /**
     * Validate và synchronize calories cho meal
     */
    void validateAndSynchronize(Meal meal);
} 