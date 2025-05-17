package com.example.workoutservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
public class Schedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private Long userId;
    
    @NotNull
    private String name;
    
    private String description;
    
    @NotNull
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledWorkout> scheduledWorkouts = new ArrayList<>();
    
    public Schedule() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
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
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public List<ScheduledWorkout> getScheduledWorkouts() {
        return scheduledWorkouts;
    }
    
    public void setScheduledWorkouts(List<ScheduledWorkout> scheduledWorkouts) {
        this.scheduledWorkouts = scheduledWorkouts;
    }
    
    public void addScheduledWorkout(ScheduledWorkout scheduledWorkout) {
        scheduledWorkouts.add(scheduledWorkout);
        scheduledWorkout.setSchedule(this);
    }
    
    public void removeScheduledWorkout(ScheduledWorkout scheduledWorkout) {
        scheduledWorkouts.remove(scheduledWorkout);
        scheduledWorkout.setSchedule(null);
    }
} 