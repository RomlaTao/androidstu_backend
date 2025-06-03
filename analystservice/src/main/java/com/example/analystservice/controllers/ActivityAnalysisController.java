package com.example.analystservice.controllers;

import com.example.analystservice.dtos.ActivityAnalysisDto;
import com.example.analystservice.dtos.TdeeCalculationDto;
import com.example.analystservice.services.ActivityFactorCalculationService;
import com.example.analystservice.services.HealthCalculationService;
import com.example.analystservice.clients.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/analytics/activity-analysis")
public class ActivityAnalysisController {

    @Autowired
    private ActivityFactorCalculationService activityFactorService;

    @Autowired
    private HealthCalculationService healthCalculationService;

    @Autowired
    private UserServiceClient userServiceClient;

    /**
     * Phân tích Activity Factor từ dữ liệu workout thực tế
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ActivityAnalysisDto> analyzeUserActivity(@PathVariable("userId") String userId) {
        try {
            ActivityAnalysisDto activityAnalysis = activityFactorService
                    .calculateActivityFactorFromWorkouts(userId);
            return ResponseEntity.ok(activityAnalysis);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Tính TDEE dựa trên dữ liệu workout thực tế
     */
    @GetMapping("/user/{userId}/workout-based-tdee")
    public Mono<ResponseEntity<TdeeCalculationDto>> calculateWorkoutBasedTdee(@PathVariable("userId") String userId) {
        return userServiceClient.getUserById(userId)
                .map(user -> {
                    try {
                        TdeeCalculationDto tdeeResult = healthCalculationService.createWorkoutBasedTdeeCalculation(user);
                        return ResponseEntity.ok(tdeeResult);
                    } catch (Exception e) {
                        return ResponseEntity.badRequest().<TdeeCalculationDto>build();
                    }
                })
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}