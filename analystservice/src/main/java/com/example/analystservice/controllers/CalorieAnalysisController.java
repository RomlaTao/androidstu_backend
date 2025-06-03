package com.example.analystservice.controllers;

import com.example.analystservice.clients.MealServiceClient;
import com.example.analystservice.clients.UserServiceClient;
import com.example.analystservice.dtos.CalorieStatsDto;
import com.example.analystservice.dtos.CalorieAnalysisDto;
import com.example.analystservice.services.CalorieAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/analytics/calorie-analysis")
@Tag(name = "Calorie Analysis", description = "APIs for analyzing calorie intake patterns and nutrition insights")
public class CalorieAnalysisController {

    private final MealServiceClient mealServiceClient;
    private final UserServiceClient userServiceClient;
    private final CalorieAnalysisService calorieAnalysisService;

    public CalorieAnalysisController(MealServiceClient mealServiceClient,
                                   UserServiceClient userServiceClient,
                                   CalorieAnalysisService calorieAnalysisService) {
        this.mealServiceClient = mealServiceClient;
        this.userServiceClient = userServiceClient;
        this.calorieAnalysisService = calorieAnalysisService;
    }

    /**
     * Lấy thống kê calories theo ngày từ MealService
     */
    @GetMapping("/daily/{userId}")
    @Operation(summary = "Get daily calorie statistics",
               description = "Get calorie intake statistics for a specific day")
    public Mono<ResponseEntity<CalorieStatsDto>> getDailyCalories(
            @Parameter(description = "User ID") @PathVariable("userId") String userId,
            @Parameter(description = "Date (YYYY-MM-DD)") 
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        return mealServiceClient.getDailyCalories(userId, date)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    /**
     * Lấy thống kê calories chi tiết theo ngày
     */
    @GetMapping("/daily/{userId}/detailed")
    @Operation(summary = "Get detailed daily calorie breakdown",
               description = "Get detailed calorie intake with meal type breakdown and meal details")
    public Mono<ResponseEntity<CalorieStatsDto>> getDetailedDailyCalories(
            @Parameter(description = "User ID") @PathVariable("userId") String userId,
            @Parameter(description = "Date (YYYY-MM-DD)")
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        return mealServiceClient.getDetailedDailyCalories(userId, date)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    /**
     * Phân tích calories theo tuần với insights
     */
    @GetMapping("/weekly/{userId}")
    @Operation(summary = "Get weekly calorie analysis",
               description = "Get 7-day calorie analysis with patterns and insights")
    public Mono<ResponseEntity<List<CalorieStatsDto>>> getWeeklyCalories(
            @Parameter(description = "User ID") @PathVariable("userId") String userId,
            @Parameter(description = "Start date of the week (YYYY-MM-DD)")
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        
        return mealServiceClient.getWeeklyCalories(userId, startDate)
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    /**
     * Phân tích calories theo tháng
     */
    @GetMapping("/monthly/{userId}")
    @Operation(summary = "Get monthly calorie analysis",
               description = "Get monthly calorie analysis with trends and patterns")
    public Mono<ResponseEntity<List<CalorieStatsDto>>> getMonthlyCalories(
            @Parameter(description = "User ID") @PathVariable("userId") String userId,
            @Parameter(description = "Year") @RequestParam("year") int year,
            @Parameter(description = "Month (1-12)") @RequestParam("month") int month) {
        
        return mealServiceClient.getMonthlyCalories(userId, year, month)
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    /**
     * Phân tích calories vs TDEE - So sánh calories nạp vào vs TDEE
     */
    @GetMapping("/vs-tdee/{userId}")
    @Operation(summary = "Compare daily calories with TDEE",
               description = "Compare calorie intake with Total Daily Energy Expenditure for weight management insights")
    public Mono<ResponseEntity<CalorieAnalysisDto>> getCaloriesVsTdee(
            @Parameter(description = "User ID") @PathVariable("userId") String userId,
            @Parameter(description = "Date (YYYY-MM-DD)")
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        return calorieAnalysisService.analyzeCaloriesVsTdee(userId, date)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    /**
     * Phân tích xu hướng calories trong khoảng thời gian
     */
    @GetMapping("/trends/{userId}")
    @Operation(summary = "Get calorie intake trends",
               description = "Analyze calorie intake trends over a custom date range")
    public Mono<ResponseEntity<List<CalorieStatsDto>>> getCalorieTrends(
            @Parameter(description = "User ID") @PathVariable("userId") String userId,
            @Parameter(description = "Start date (YYYY-MM-DD)")
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)")
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        return mealServiceClient.getCaloriesInRange(userId, startDate, endDate)
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }
} 