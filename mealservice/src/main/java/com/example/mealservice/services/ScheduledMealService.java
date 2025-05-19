package com.example.mealservice.services;

import com.example.mealservice.dtos.ScheduledMealDTO;
import com.example.mealservice.entities.MealStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledMealService {
    
    ScheduledMealDTO createScheduledMeal(ScheduledMealDTO scheduledMealDTO);
    
    ScheduledMealDTO getScheduledMealById(Long id);
    
    List<ScheduledMealDTO> getScheduledMealsByScheduleId(Long scheduleId);
    
    List<ScheduledMealDTO> getUserMealsInDateRange(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    List<ScheduledMealDTO> getUserMealsInDateRangeWithStatus(Long userId, MealStatus status,
                                               LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    ScheduledMealDTO updateScheduledMeal(Long id, ScheduledMealDTO scheduledMealDTO);
    
    ScheduledMealDTO updateMealStatus(Long id, MealStatus status);
    
    void deleteScheduledMeal(Long id);
} 