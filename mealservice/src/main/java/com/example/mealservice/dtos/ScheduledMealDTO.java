package com.example.mealservice.dtos;

import com.example.mealservice.enums.MealStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ScheduledMealDTO {
    
    private Long id;
    
    private Long scheduleId;
    
    @NotNull(message = "Meal ID is required")
    private Long mealId;
    
    private MealDTO meal;
    
    @NotNull(message = "Scheduled date and time is required")
    private LocalDateTime scheduledDateTime;
    
    private MealStatus status;
    
    private String notes;
    
    public ScheduledMealDTO() {
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
    
    public Long getMealId() {
        return mealId;
    }
    
    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }
    
    public MealDTO getMeal() {
        return meal;
    }
    
    public void setMeal(MealDTO meal) {
        this.meal = meal;
    }
    
    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }
    
    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }
    
    public MealStatus getStatus() {
        return status;
    }
    
    public void setStatus(MealStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
} 