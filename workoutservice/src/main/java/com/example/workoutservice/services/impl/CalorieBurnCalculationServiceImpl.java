package com.example.workoutservice.services.impl;

import com.example.workoutservice.dtos.CalorieBurnStatsDto;
import com.example.workoutservice.entities.ScheduledWorkout;
import com.example.workoutservice.enums.WorkoutStatus;
import com.example.workoutservice.enums.WorkoutType;
import com.example.workoutservice.repositories.ScheduledWorkoutRepository;
import com.example.workoutservice.services.CalorieBurnCalculationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalorieBurnCalculationServiceImpl implements CalorieBurnCalculationService {
    
    private final ScheduledWorkoutRepository scheduledWorkoutRepository;
    
    public CalorieBurnCalculationServiceImpl(ScheduledWorkoutRepository scheduledWorkoutRepository) {
        this.scheduledWorkoutRepository = scheduledWorkoutRepository;
    }
    
    @Override
    public CalorieBurnStatsDto calculateDailyCaloriesBurned(String userId, LocalDate date) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        
        Integer totalCaloriesBurned = scheduledWorkoutRepository.calculateDailyCaloriesBurned(
            userId, startOfDay, endOfDay, WorkoutStatus.COMPLETED);
        
        // Ensure null safety
        totalCaloriesBurned = totalCaloriesBurned != null ? totalCaloriesBurned : 0;
        
        return new CalorieBurnStatsDto(userId, date, totalCaloriesBurned);
    }
    
    @Override
    public List<CalorieBurnStatsDto> calculateWeeklyCaloriesBurned(String userId, LocalDate startDate) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        
        LocalDate endDate = startDate.plusDays(6);
        return calculateCaloriesBurnedInRange(userId, startDate, endDate);
    }
    
    @Override
    public List<CalorieBurnStatsDto> calculateMonthlyCaloriesBurned(String userId, int year, int month) {
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
        
        return calculateCaloriesBurnedInRange(userId, startDate, endDate);
    }

    @Override
    public List<CalorieBurnStatsDto> calculateCaloriesBurnedInRange(String userId, LocalDate startDate, LocalDate endDate) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        
        // Optimized batch query instead of multiple daily queries
        LocalDateTime rangeStart = startDate.atStartOfDay();
        LocalDateTime rangeEnd = endDate.plusDays(1).atStartOfDay();
        
        List<Object[]> batchResults = scheduledWorkoutRepository.calculateCaloriesBurnedInDateRange(
            userId, rangeStart, rangeEnd, WorkoutStatus.COMPLETED);
        
        List<CalorieBurnStatsDto> rangeStats = new ArrayList<>();
        
        for (Object[] result : batchResults) {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate();
            Integer totalCalories = result[1] != null ? ((Number) result[1]).intValue() : 0;
            
            rangeStats.add(new CalorieBurnStatsDto(userId, date, totalCalories));
        }
        
        // Fill missing dates with zero calories
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            final LocalDate checkDate = currentDate;
            boolean dateExists = rangeStats.stream()
                    .anyMatch(stats -> stats.getDate().equals(checkDate));
            
            if (!dateExists) {
                rangeStats.add(new CalorieBurnStatsDto(userId, checkDate, 0));
            }
            
            currentDate = currentDate.plusDays(1);
        }
        
        // Sort by date
        rangeStats.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        
        return rangeStats;
    }
} 