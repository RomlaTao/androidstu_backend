package com.example.workoutservice.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class ScheduleDTO {
    
    private Long id;
    
    @NotNull(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Schedule name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private List<ScheduledWorkoutDTO> scheduledWorkouts;
    
    public ScheduleDTO() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
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
    
    public List<ScheduledWorkoutDTO> getScheduledWorkouts() {
        return scheduledWorkouts;
    }
    
    public void setScheduledWorkouts(List<ScheduledWorkoutDTO> scheduledWorkouts) {
        this.scheduledWorkouts = scheduledWorkouts;
    }
} 