package com.example.mealservice.services;

import com.example.mealservice.dtos.ScheduledMealDTO;
import com.example.mealservice.enums.MealStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledMealService {
    
    ScheduledMealDTO createScheduledMeal(ScheduledMealDTO scheduledMealDTO);
    
    ScheduledMealDTO getScheduledMealById(Long id);
    
    List<ScheduledMealDTO> getScheduledMealsByScheduleId(Long scheduleId);
    
    List<ScheduledMealDTO> getUserMealsInDateRange(String userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    List<ScheduledMealDTO> getUserMealsInDateRangeWithStatus(String userId, MealStatus status,
                                               LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    ScheduledMealDTO updateScheduledMeal(Long id, ScheduledMealDTO scheduledMealDTO);
    
    ScheduledMealDTO updateMealStatus(Long id, MealStatus status);
    
    void deleteScheduledMeal(Long id);
} 