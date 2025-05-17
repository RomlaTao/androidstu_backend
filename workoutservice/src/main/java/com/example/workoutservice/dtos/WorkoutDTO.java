package com.example.workoutservice.dtos;

import com.example.workoutservice.entities.WorkoutType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class WorkoutDTO {
    
    private Long id;
    
    @NotBlank(message = "Workout name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;
    
    @NotNull(message = "Calories burned is required")
    @Min(value = 0, message = "Calories burned must be a positive number")
    private Integer caloriesBurned;
    
    @NotNull(message = "Workout type is required")
    private WorkoutType type;
    
    private List<ExerciseDTO> exercises;
    
    public WorkoutDTO() {
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
    
    public List<ExerciseDTO> getExercises() {
        return exercises;
    }
    
    public void setExercises(List<ExerciseDTO> exercises) {
        this.exercises = exercises;
    }
} 