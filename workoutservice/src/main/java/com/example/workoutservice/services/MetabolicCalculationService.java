package com.example.workoutservice.services;

import com.example.workoutservice.entities.Exercise;
import com.example.workoutservice.entities.Workout;
import com.example.workoutservice.enums.WorkoutType;

/**
 * Service để tính toán calories đốt cháy dựa trên MET (Metabolic Equivalent of Task)
 */
public interface MetabolicCalculationService {
    
    /**
     * Tính calories đốt cháy cho workout dựa trên MET value và userId
     * Formula: Calories = MET × weight(kg) × duration(hours)
     * Weight được lấy từ UserService theo userId
     * 
     * @param workout Workout entity
     * @param userId ID của user để lấy weight
     * @return Calories đốt cháy
     */
    Integer calculateWorkoutCalories(Workout workout, Long userId);
    
    /**
     * Tính calories cho một exercise cụ thể với userId
     */
    Integer calculateExerciseCalories(Exercise exercise, WorkoutType workoutType, Long userId);
    
    /**
     * Validate và synchronize calories cho workout với userId
     */
    void validateAndSynchronizeWorkoutCalories(Workout workout, Long userId);
    
    /**
     * Tính calories với weight cụ thể (fallback method)
     * @param workout Workout entity
     * @param userWeightKg Cân nặng cụ thể (kg)
     * @return Calories đốt cháy
     */
    Integer calculateWorkoutCaloriesWithWeight(Workout workout, Double userWeightKg);
} 