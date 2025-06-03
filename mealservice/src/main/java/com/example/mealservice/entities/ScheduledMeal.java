package com.example.mealservice.entities;

import com.example.mealservice.enums.MealStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_meals")
public class ScheduledMeal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    
    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;
    
    @NotNull
    private LocalDateTime scheduledDateTime;
    
    @Enumerated(EnumType.STRING)
    private MealStatus status;
    
    private String notes;
    
    public ScheduledMeal() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Schedule getSchedule() {
        return schedule;
    }
    
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
    
    public Meal getMeal() {
        return meal;
    }
    
    public void setMeal(Meal meal) {
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