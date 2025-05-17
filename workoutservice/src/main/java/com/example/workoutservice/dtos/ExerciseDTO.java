package com.example.workoutservice.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ExerciseDTO {
    
    private Long id;
    
    @NotBlank(message = "Exercise name is required")
    private String name;
    
    private String description;
    
    @Min(value = 0, message = "Sets must be a non-negative number")
    private Integer sets;
    
    @Min(value = 0, message = "Reps must be a non-negative number")
    private Integer reps;
    
    @Min(value = 0, message = "Duration must be a non-negative number")
    private Integer durationSeconds;
    
    public ExerciseDTO() {
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
    
    public Integer getSets() {
        return sets;
    }
    
    public void setSets(Integer sets) {
        this.sets = sets;
    }
    
    public Integer getReps() {
        return reps;
    }
    
    public void setReps(Integer reps) {
        this.reps = reps;
    }
    
    public Integer getDurationSeconds() {
        return durationSeconds;
    }
    
    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
} 