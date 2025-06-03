package com.example.workoutservice.controllers;

import com.example.workoutservice.dtos.CalorieBurnStatsDto;
import com.example.workoutservice.services.CalorieBurnCalculationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/workouts/calories-burned")
public class CaloriesBurnController {
    
    private final CalorieBurnCalculationService calorieBurnCalculationService;
    
    public CaloriesBurnController(CalorieBurnCalculationService calorieBurnCalculationService) {
        this.calorieBurnCalculationService = calorieBurnCalculationService;
    }
    
    /**
     * Lấy thống kê calories đốt cháy theo ngày
     */
    @GetMapping("/daily/{userId}")
    public ResponseEntity<CalorieBurnStatsDto> getDailyCaloriesBurned(
            @PathVariable("userId") String userId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        CalorieBurnStatsDto stats = calorieBurnCalculationService.calculateDailyCaloriesBurned(userId, date);
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Lấy thống kê calories đốt cháy theo tuần (7 ngày)
     */
    @GetMapping("/weekly/{userId}")
    public ResponseEntity<List<CalorieBurnStatsDto>> getWeeklyCaloriesBurned(
            @PathVariable("userId") String userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        
        List<CalorieBurnStatsDto> stats = calorieBurnCalculationService.calculateWeeklyCaloriesBurned(userId, startDate);
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Lấy thống kê calories đốt cháy theo tháng
     */
    @GetMapping("/monthly/{userId}")
    public ResponseEntity<List<CalorieBurnStatsDto>> getMonthlyCaloriesBurned(
            @PathVariable("userId") String userId,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        
        List<CalorieBurnStatsDto> stats = calorieBurnCalculationService.calculateMonthlyCaloriesBurned(userId, year, month);
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Lấy thống kê calories đốt cháy theo khoảng thời gian tùy chọn
     * Endpoint này để consistency với mealservice API
     */
    @GetMapping("/range/{userId}")
    public ResponseEntity<List<CalorieBurnStatsDto>> getCaloriesBurnedInRange(
            @PathVariable("userId") String userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<CalorieBurnStatsDto> stats = calorieBurnCalculationService.calculateCaloriesBurnedInRange(userId, startDate, endDate);
        return ResponseEntity.ok(stats);
    }
} 