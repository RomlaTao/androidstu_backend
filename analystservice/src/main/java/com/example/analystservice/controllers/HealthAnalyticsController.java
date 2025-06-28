package com.example.analystservice.controllers;

import com.example.analystservice.dtos.HealthMetricsDto;
import com.example.analystservice.dtos.TdeeCalculationDto;
import com.example.analystservice.enums.ActivityLevel;
import com.example.analystservice.services.HealthCalculationService;
import com.example.analystservice.clients.UserServiceClient;
import com.example.analystservice.strategies.TdeeCalculationStrategy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/analytics/health-analytics")
@Tag(name = "Health Analytics", description = "APIs for calculating BMI, BMR, and TDEE")
public class HealthAnalyticsController {

    private final HealthCalculationService calculationService;
    private final UserServiceClient userServiceClient;
    private final TdeeCalculationStrategy tdeeCalculationStrategy;

    public HealthAnalyticsController(HealthCalculationService calculationService,
                                     UserServiceClient userServiceClient,
                                     TdeeCalculationStrategy tdeeCalculationStrategy) {
        this.calculationService = calculationService;
        this.userServiceClient = userServiceClient;
        this.tdeeCalculationStrategy = tdeeCalculationStrategy;
    }

    @GetMapping("/health-metrics/{userId}")
    @Operation(summary = "Get health metrics for a user",
            description = "Calculate BMI, BMR and provide health recommendations for a specific user")
    public Mono<ResponseEntity<HealthMetricsDto>> getHealthMetrics(
            @Parameter(description = "User ID") @PathVariable("userId") String userId) {

        return userServiceClient.getUserById(userId)
                .map(user -> {
                    try {
                        HealthMetricsDto metrics = calculationService.createHealthMetrics(user);
                        return ResponseEntity.ok(metrics);
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().<HealthMetricsDto>build();
                    }
                })
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping("/tdee/{userId}")
    @Operation(summary = "Calculate TDEE for a user",
            description = "Calculate Total Daily Energy Expenditure with different activity levels")
    public Mono<ResponseEntity<TdeeCalculationDto>> calculateTdee(
            @Parameter(description = "User ID") @PathVariable("userId") String userId,
            @Parameter(description = "Activity level")
            @RequestParam(value = "activityLevel", defaultValue = "MODERATELY_ACTIVE") ActivityLevel activityLevel) {

        return userServiceClient.getUserById(userId)
                .map(user -> {
                    try {
                        TdeeCalculationDto tdeeCalculation = calculationService.createTdeeCalculation(user, activityLevel);
                        return ResponseEntity.ok(tdeeCalculation);
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().<TdeeCalculationDto>build();
                    }
                })
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @PostMapping("/calculate-bmi")
    @Operation(summary = "Calculate BMI directly",
            description = "Calculate BMI from height and weight without user data")
    public ResponseEntity<Double> calculateBmi(
            @Parameter(description = "Weight in kg") @RequestParam("weight") double weight,
            @Parameter(description = "Height in cm") @RequestParam("height") double height) {

        try {
            double bmi = calculationService.calculateBmi(weight, height);
            return ResponseEntity.ok(Math.round(bmi * 100.0) / 100.0);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/calculate-bmr")
    @Operation(summary = "Calculate BMR directly",
            description = "Calculate BMR from basic parameters without user data")
    public ResponseEntity<Double> calculateBmr(
            @Parameter(description = "Weight in kg") @RequestParam("weight") double weight,
            @Parameter(description = "Height in cm") @RequestParam("height") double height,
            @Parameter(description = "Age in years") @RequestParam("age") int age,
            @Parameter(description = "Gender (MALE/FEMALE)") @RequestParam("gender") String gender) {

        try {
            double bmr = calculationService.calculateBmr(weight, height, age, gender);
            return ResponseEntity.ok(Math.round(bmr * 100.0) / 100.0);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tdee/{userId}/strategy")
    public Mono<ResponseEntity<TdeeCalculationDto>> calculateTdeeWithStrategy(@PathVariable("userId") String userId) {
        return userServiceClient.getUserById(userId)
                .map(user -> {
                    TdeeCalculationDto tdeeCalculation = tdeeCalculationStrategy.calculateTdeeWithStrategy(user);
                    return ResponseEntity.ok(tdeeCalculation);
                })
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @PostMapping("/tdee/{userId}/manual")
    public Mono<ResponseEntity<TdeeCalculationDto>> calculateTdeeManual(
            @PathVariable("userId") String userId,
            @RequestParam("activityLevel") ActivityLevel activityLevel) {
        
        return userServiceClient.getUserById(userId)
                .map(user -> {
                    TdeeCalculationDto tdeeCalculation = calculationService.createTdeeCalculation(user, activityLevel);
                    tdeeCalculation.setCalculationMethod("Manual Activity Level");
                    tdeeCalculation.setMethodDescription("Dựa trên mức độ hoạt động bạn đã chọn");
                    return ResponseEntity.ok(tdeeCalculation);
                })
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping("/tdee/{userId}/user-input")
    @Operation(summary = "Get TDEE calculation based on user input",
            description = "Calculate TDEE using the value provided by the user")
    public Mono<ResponseEntity<TdeeCalculationDto>> getUserInputTdee(@PathVariable("userId") String userId) {
        return userServiceClient.getUserById(userId)
                .map(user -> {
                    try {
                        TdeeCalculationDto tdeeCalculation = calculationService.createUserInputTdeeCalculation(user);
                        return ResponseEntity.ok(tdeeCalculation);
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().<TdeeCalculationDto>build();
                    }
                })
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}