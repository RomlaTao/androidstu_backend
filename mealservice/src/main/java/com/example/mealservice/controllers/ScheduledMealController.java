package com.example.mealservice.controllers;

import com.example.mealservice.dtos.ScheduledMealDTO;
import com.example.mealservice.entities.MealStatus;
import com.example.mealservice.services.ScheduledMealService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/meals/scheduled-meals")
public class ScheduledMealController {
    
    private final ScheduledMealService scheduledMealService;
    
    public ScheduledMealController(ScheduledMealService scheduledMealService) {
        this.scheduledMealService = scheduledMealService;
    }
    
    @PostMapping
    public ResponseEntity<ScheduledMealDTO> createScheduledMeal(
            @Valid @RequestBody ScheduledMealDTO scheduledMealDTO) {
        return new ResponseEntity<>(
            scheduledMealService.createScheduledMeal(scheduledMealDTO),
            HttpStatus.CREATED
        );
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ScheduledMealDTO> getScheduledMealById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduledMealService.getScheduledMealById(id));
    }
    
    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<ScheduledMealDTO>> getScheduledMealsByScheduleId(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduledMealService.getScheduledMealsByScheduleId(scheduleId));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ScheduledMealDTO>> getUserMealsInDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        return ResponseEntity.ok(
            scheduledMealService.getUserMealsInDateRange(userId, startDateTime, endDateTime)
        );
    }
    
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<ScheduledMealDTO>> getUserMealsInDateRangeWithStatus(
            @PathVariable Long userId,
            @PathVariable MealStatus status,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        return ResponseEntity.ok(
            scheduledMealService.getUserMealsInDateRangeWithStatus(
                userId, status, startDateTime, endDateTime
            )
        );
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ScheduledMealDTO> updateScheduledMeal(
            @PathVariable Long id, 
            @Valid @RequestBody ScheduledMealDTO scheduledMealDTO) {
        return ResponseEntity.ok(scheduledMealService.updateScheduledMeal(id, scheduledMealDTO));
    }
    
    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<ScheduledMealDTO> updateMealStatus(
            @PathVariable Long id,
            @PathVariable MealStatus status) {
        return ResponseEntity.ok(scheduledMealService.updateMealStatus(id, status));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduledMeal(@PathVariable Long id) {
        scheduledMealService.deleteScheduledMeal(id);
        return ResponseEntity.noContent().build();
    }
}
