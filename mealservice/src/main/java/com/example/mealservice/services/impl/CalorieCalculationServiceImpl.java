package com.example.mealservice.services.impl;

import com.example.mealservice.dtos.CalorieStatsDto;
import com.example.mealservice.enums.MealStatus;
import com.example.mealservice.repositories.ScheduledMealRepository;
import com.example.mealservice.services.CalorieCalculationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalorieCalculationServiceImpl implements CalorieCalculationService {
    
    private final ScheduledMealRepository scheduledMealRepository;
    
    public CalorieCalculationServiceImpl(ScheduledMealRepository scheduledMealRepository) {
        this.scheduledMealRepository = scheduledMealRepository;
    }
    
    @Override
    public CalorieStatsDto calculateDailyCalories(String userId, LocalDate date) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        
        // Calculate total calories
        Integer totalCalories = scheduledMealRepository.calculateDailyCalories(
            userId, startOfDay, endOfDay, MealStatus.COMPLETED);
        
        // Ensure null safety
        totalCalories = totalCalories != null ? totalCalories : 0;
        
        return new CalorieStatsDto(userId, date, totalCalories);
    }
    
    @Override
    public List<CalorieStatsDto> calculateWeeklyCalories(String userId, LocalDate startDate) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        
        List<CalorieStatsDto> weeklyStats = new ArrayList<>();
        
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            CalorieStatsDto dailyStats = calculateDailyCalories(userId, currentDate);
            weeklyStats.add(dailyStats);
        }
        
        return weeklyStats;
    }
    
    @Override
    public List<CalorieStatsDto> calculateMonthlyCalories(String userId, int year, int month) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        if (year < 1900 || year > 2100) {
            throw new IllegalArgumentException("Year must be between 1900 and 2100");
        }
        
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        return calculateCaloriesInRange(userId, startDate, endDate);
    }
    
    @Override
    public List<CalorieStatsDto> calculateCaloriesInRange(String userId, LocalDate startDate, LocalDate endDate) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        
        List<CalorieStatsDto> rangeStats = new ArrayList<>();
        
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            CalorieStatsDto dailyStats = calculateDailyCalories(userId, currentDate);
            rangeStats.add(dailyStats);
            currentDate = currentDate.plusDays(1);
        }
        
        return rangeStats;
    }
} 