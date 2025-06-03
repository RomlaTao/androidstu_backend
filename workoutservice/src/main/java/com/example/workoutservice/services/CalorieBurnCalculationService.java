package com.example.workoutservice.services;

import com.example.workoutservice.dtos.CalorieBurnStatsDto;
import java.time.LocalDate;
import java.util.List;

public interface CalorieBurnCalculationService {
    
    /**
     * Tính tổng calories đốt cháy trong một ngày
     */
    CalorieBurnStatsDto calculateDailyCaloriesBurned(String userId, LocalDate date);
    
    /**
     * Tính tổng calories đốt cháy trong một tuần
     */
    List<CalorieBurnStatsDto> calculateWeeklyCaloriesBurned(String userId, LocalDate startDate);

    /**
     * Tính tổng calories đốt cháy trong một tháng
     */
    List<CalorieBurnStatsDto> calculateMonthlyCaloriesBurned(String userId, int year, int month);

    /**
     * Tính tổng calories đốt cháy trong khoảng thời gian tùy chỉnh
     */
    List<CalorieBurnStatsDto> calculateCaloriesBurnedInRange(String userId, LocalDate startDate, LocalDate endDate);
}