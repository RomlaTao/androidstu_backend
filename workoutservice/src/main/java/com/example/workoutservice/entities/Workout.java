package com.example.workoutservice.entities;

import com.example.workoutservice.enums.WorkoutType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "workouts")
public class Workout {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @NotNull
    @Min(value = 1, message = "Duration must be positive")
    private Integer durationMinutes;
    
    @Min(value = 0, message = "Calories burned cannot be negative")
    private Integer caloriesBurned;
    
    @Enumerated(EnumType.STRING)
    private WorkoutType type;
    
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Exercise> exercises = new HashSet<>();
    
    public Workout() {
    }
    
    /**
     * Calculate calories using MET values với weight cụ thể
     * Formula: Calories = MET × weight(kg) × duration(hours)
     * 
     * NOTE: Method này không gọi UserService để tránh circular dependency trong JPA lifecycle
     * Calorie calculation với userId thực hiện trong MetabolicCalculationService
     */
    public void updateCaloriesFromMET(Double userWeightKg) {
        if (type != null && durationMinutes != null && durationMinutes > 0 && userWeightKg != null) {
            Double metValue = type.getMetValue();
            Double durationHours = durationMinutes / 60.0;
            
            Integer metBasedCalories = (int) (metValue * userWeightKg * durationHours);
            
            // If we have exercises, consider exercise-based calculation too
            Integer exerciseBasedCalories = calculateCaloriesFromExercises(userWeightKg);
            
            // Use the higher value (MET-based vs exercise-based)
            this.caloriesBurned = Math.max(metBasedCalories, exerciseBasedCalories);
        } else {
            // Set to 0 if we can't calculate, will be updated by service layer
            this.caloriesBurned = 0;
        }
    }
    
    /**
     * Calculate calories from exercises if available
     */
    public Integer calculateCaloriesFromExercises(Double userWeightKg) {
        if (exercises == null || exercises.isEmpty() || type == null || userWeightKg == null) {
            return 0;
        }
        
        Double metValue = type.getMetValue();
        
        return exercises.stream()
                .mapToInt(exercise -> {
                    // Calculate based on exercise duration if available
                    if (exercise.getDurationSeconds() != null && exercise.getDurationSeconds() > 0) {
                        Double durationHours = exercise.getDurationSeconds() / 3600.0;
                        return (int) (metValue * userWeightKg * durationHours);
                    }
                    
                    // For strength exercises: estimate based on sets and reps
                    if (exercise.getSets() != null && exercise.getReps() != null) {
                        // Estimate: each set takes about 1-2 minutes
                        Double estimatedMinutes = exercise.getSets() * 1.5;
                        Double durationHours = estimatedMinutes / 60.0;
                        return (int) (metValue * userWeightKg * durationHours);
                    }
                    
                    return 0;
                })
                .sum();
    }
    
    /**
     * Validate that calories are properly calculated với weight cụ thể
     */
    public boolean isCalorieCalculationValid(Double userWeightKg) {
        if (userWeightKg == null) {
            return false;
        }
        
        Integer expectedCalories = calculateExpectedCalories(userWeightKg);
        return Math.abs(this.caloriesBurned - expectedCalories) <= 10; // Allow 10 calorie tolerance
    }
    
    /**
     * Calculate expected calories for validation
     */
    private Integer calculateExpectedCalories(Double userWeightKg) {
        if (type == null || durationMinutes == null || userWeightKg == null) {
            return 0;
        }
        
        Double metValue = type.getMetValue();
        Double durationHours = durationMinutes / 60.0;
        
        return (int) (metValue * userWeightKg * durationHours);
    }
    
    // Standard getters and setters
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
    
    public Integer getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public Integer getCaloriesBurned() {
        return caloriesBurned;
    }
    
    public void setCaloriesBurned(Integer caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }
    
    public WorkoutType getType() {
        return type;
    }
    
    public void setType(WorkoutType type) {
        this.type = type;
    }
    
    public Set<Exercise> getExercises() {
        return exercises;
    }
    
    public void setExercises(Set<Exercise> exercises) {
        this.exercises = exercises;
    }
    
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
        exercise.setWorkout(this);
        // Note: Recalculation sẽ được thực hiện trong service layer với userId
    }
    
    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
        exercise.setWorkout(null);
        // Note: Recalculation sẽ được thực hiện trong service layer với userId
    }
} 