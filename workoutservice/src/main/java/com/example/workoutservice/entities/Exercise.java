package com.example.workoutservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "exercises")
public class  Exercise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @Min(0)
    private Integer sets;
    
    @Min(0)
    private Integer reps;
    
    @Min(0)
    private Integer durationSeconds;
    
    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;
    
    public Exercise() {
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
    
    public Workout getWorkout() {
        return workout;
    }
    
    public void setWorkout(Workout workout) {
        this.workout = workout;
    }
} 