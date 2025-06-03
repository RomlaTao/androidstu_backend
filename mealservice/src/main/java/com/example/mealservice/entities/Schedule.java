package com.example.mealservice.entities;

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
    @Column(length = 36)
    private String userId;
    
    @NotNull
    private String name;
    
    private String description;
    
    @NotNull
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledMeal> scheduledMeals = new ArrayList<>();
    
    public Schedule() {
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
    
    public List<ScheduledMeal> getScheduledMeals() {
        return scheduledMeals;
    }
    
    public void setScheduledMeals(List<ScheduledMeal> scheduledMeals) {
        this.scheduledMeals = scheduledMeals;
    }
    
    public void addScheduledMeal(ScheduledMeal scheduledMeal) {
        scheduledMeals.add(scheduledMeal);
        scheduledMeal.setSchedule(this);
    }
    
    public void removeScheduledMeal(ScheduledMeal scheduledMeal) {
        scheduledMeals.remove(scheduledMeal);
        scheduledMeal.setSchedule(null);
    }
} 