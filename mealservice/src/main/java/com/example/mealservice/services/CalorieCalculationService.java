package com.example.mealservice.services;

import com.example.mealservice.dtos.CalorieStatsDto;
import java.time.LocalDate;
import java.util.List;

public interface CalorieCalculationService {
    
    /**
     * Tính tổng calories nạp vào trong một ngày
     */
    CalorieStatsDto calculateDailyCalories(String userId, LocalDate date);
    
    /**
     * Tính tổng calories nạp vào trong một tuần
     */
    List<CalorieStatsDto> calculateWeeklyCalories(String userId, LocalDate startDate);
    
    /**
     * Tính tổng calories nạp vào trong một tháng
     */
    List<CalorieStatsDto> calculateMonthlyCalories(String userId, int year, int month);
    
    /**
     * Tính tổng calories trong khoảng thời gian tùy chỉnh
     */
    List<CalorieStatsDto> calculateCaloriesInRange(String userId, LocalDate startDate, LocalDate endDate);
} 