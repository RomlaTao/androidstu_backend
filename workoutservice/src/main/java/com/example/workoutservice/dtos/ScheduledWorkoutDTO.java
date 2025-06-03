package com.example.workoutservice.dtos;

import com.example.workoutservice.enums.WorkoutStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ScheduledWorkoutDTO {
    
    private Long id;
    
    private Long scheduleId;
    
    @NotNull(message = "Workout ID is required")
    private Long workoutId;
    
    private WorkoutDTO workout;
    
    @NotNull(message = "Scheduled date and time is required")
    private LocalDateTime scheduledDateTime;
    
    private WorkoutStatus status;
    
    private String notes;
    
    public ScheduledWorkoutDTO() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getScheduleId() {
        return scheduleId;
    }
    
    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
    
    public Long getWorkoutId() {
        return workoutId;
    }
    
    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }
    
    public WorkoutDTO getWorkout() {
        return workout;
    }
    
    public void setWorkout(WorkoutDTO workout) {
        this.workout = workout;
    }
    
    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }
    
    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }
    
    public WorkoutStatus getStatus() {
        return status;
    }
    
    public void setStatus(WorkoutStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
} 