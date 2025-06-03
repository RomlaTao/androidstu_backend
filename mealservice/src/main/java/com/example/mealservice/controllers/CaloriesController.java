package com.example.mealservice.controllers;

import com.example.mealservice.dtos.CalorieStatsDto;
import com.example.mealservice.services.CalorieCalculationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/meals/calories")
public class CaloriesController {
    
    private final CalorieCalculationService calorieCalculationService;
    
    public CaloriesController(CalorieCalculationService calorieCalculationService) {
        this.calorieCalculationService = calorieCalculationService;
    }
    
    /**
     * Lấy thống kê calories theo ngày
     */
    @GetMapping("/daily/{userId}")
    public ResponseEntity<CalorieStatsDto> getDailyCalories(
            @PathVariable("userId") String userId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        CalorieStatsDto stats = calorieCalculationService.calculateDailyCalories(userId, date);
        return ResponseEntity.ok(stats);
    }

    /**
     * Lấy thống kê calories theo tuần (7 ngày)
     */
    @GetMapping("/weekly/{userId}")
    public ResponseEntity<List<CalorieStatsDto>> getWeeklyCalories(
            @PathVariable("userId") String userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        
        List<CalorieStatsDto> stats = calorieCalculationService.calculateWeeklyCalories(userId, startDate);
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Lấy thống kê calories theo tháng
     */
    @GetMapping("/monthly/{userId}")
    public ResponseEntity<List<CalorieStatsDto>> getMonthlyCalories(
            @PathVariable("userId") String userId,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        
        List<CalorieStatsDto> stats = calorieCalculationService.calculateMonthlyCalories(userId, year, month);
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Lấy thống kê calories trong khoảng thời gian tùy chỉnh
     */
    @GetMapping("/range/{userId}")
    public ResponseEntity<List<CalorieStatsDto>> getCaloriesInRange(
            @PathVariable("userId") String userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<CalorieStatsDto> stats = calorieCalculationService.calculateCaloriesInRange(userId, startDate, endDate);
        return ResponseEntity.ok(stats);
    }
} 