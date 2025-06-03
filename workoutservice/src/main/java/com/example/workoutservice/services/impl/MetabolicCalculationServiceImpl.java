package com.example.workoutservice.services.impl;

import com.example.workoutservice.entities.Exercise;
import com.example.workoutservice.entities.Workout;
import com.example.workoutservice.enums.WorkoutType;
import com.example.workoutservice.services.MetabolicCalculationService;
import com.example.workoutservice.services.UserIntegrationService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MetabolicCalculationServiceImpl implements MetabolicCalculationService {
    
    private static final Logger logger = LoggerFactory.getLogger(MetabolicCalculationServiceImpl.class);
    private static final Double DEFAULT_WEIGHT_KG = 70.0; // Fallback weight
    
    private final UserIntegrationService userIntegrationService;
    
    public MetabolicCalculationServiceImpl(UserIntegrationService userIntegrationService) {
        this.userIntegrationService = userIntegrationService;
    }
    
    @Override
    public Integer calculateWorkoutCalories(Workout workout, Long userId) {
        if (workout == null || workout.getType() == null || workout.getDurationMinutes() == null) {
            return 0;
        }
        
        // Get user weight from UserService
        Double weight = userIntegrationService.getCachedUserWeight(userId);
        
        return calculateWorkoutCaloriesWithWeight(workout, weight);
    }
    
    @Override
    public Integer calculateWorkoutCaloriesWithWeight(Workout workout, Double userWeightKg) {
        if (workout == null || workout.getType() == null || workout.getDurationMinutes() == null) {
            return 0;
        }
        
        Double weight = userWeightKg != null ? userWeightKg : DEFAULT_WEIGHT_KG;
        Double metValue = workout.getType().getMetValue();
        Double durationHours = workout.getDurationMinutes() / 60.0;
        
        // Formula: Calories = MET × weight(kg) × duration(hours)
        Double calories = metValue * weight * durationHours;
        
        logger.debug("Calculated calories: {} for workout: {} (MET: {}, weight: {}kg, duration: {}min)", 
                    calories.intValue(), workout.getName(), metValue, weight, workout.getDurationMinutes());
        
        return calories.intValue();
    }
    
    @Override
    public Integer calculateExerciseCalories(Exercise exercise, WorkoutType workoutType, Long userId) {
        if (exercise == null || workoutType == null) {
            return 0;
        }
        
        // Get user weight from UserService
        Double weight = userIntegrationService.getCachedUserWeight(userId);
        Double metValue = workoutType.getMetValue();
        
        // Calculate based on exercise duration if available
        if (exercise.getDurationSeconds() != null && exercise.getDurationSeconds() > 0) {
            Double durationHours = exercise.getDurationSeconds() / 3600.0;
            Double calories = metValue * weight * durationHours;
            
            logger.debug("Calculated exercise calories from duration: {} for exercise: {} ({}s)", 
                        calories.intValue(), exercise.getName(), exercise.getDurationSeconds());
            
            return calories.intValue();
        }
        
        // For strength exercises: estimate based on sets and reps
        if (exercise.getSets() != null && exercise.getReps() != null) {
            // Estimate: each set takes about 1-2 minutes
            Double estimatedMinutes = exercise.getSets() * 1.5;
            Double durationHours = estimatedMinutes / 60.0;
            Double calories = metValue * weight * durationHours;
            
            logger.debug("Calculated exercise calories from sets/reps: {} for exercise: {} ({}x{} sets)", 
                        calories.intValue(), exercise.getName(), exercise.getSets(), exercise.getReps());
            
            return calories.intValue();
        }
        
        return 0;
    }
    
    @Override
    public void validateAndSynchronizeWorkoutCalories(Workout workout, Long userId) {
        if (workout == null) {
            return;
        }
        
        // Auto-calculate calories from workout basic info
        Integer calculatedCalories = calculateWorkoutCalories(workout, userId);
        
        // If workout has exercises, also consider their calories
        if (workout.getExercises() != null && !workout.getExercises().isEmpty()) {
            Integer exerciseCalories = workout.getExercises().stream()
                    .mapToInt(exercise -> calculateExerciseCalories(exercise, workout.getType(), userId))
                    .sum();
            
            // Use the higher value (basic calculation vs exercise-based calculation)
            calculatedCalories = Math.max(calculatedCalories, exerciseCalories);
            
            logger.debug("Workout calories: MET-based: {}, Exercise-based: {}, Final: {}", 
                        calculateWorkoutCalories(workout, userId), exerciseCalories, calculatedCalories);
        }
        
        // Update workout calories
        workout.setCaloriesBurned(calculatedCalories);
        
        logger.info("Synchronized workout calories: {} for workout: {} (userId: {})", 
                   calculatedCalories, workout.getName(), userId);
    }
} 