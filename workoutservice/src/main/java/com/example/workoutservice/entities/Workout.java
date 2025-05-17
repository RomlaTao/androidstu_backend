package com.example.workoutservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private Integer durationMinutes;
    
    @NotNull
    private Integer caloriesBurned;
    
    @Enumerated(EnumType.STRING)
    private WorkoutType type;
    
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Exercise> exercises = new HashSet<>();
    
    public Workout() {
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
    
    public Set<Exercise> getExercises() {
        return exercises;
    }
    
    public void setExercises(Set<Exercise> exercises) {
        this.exercises = exercises;
    }
    
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
        exercise.setWorkout(this);
    }
    
    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
        exercise.setWorkout(null);
    }
} 