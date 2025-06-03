package com.example.mealservice.controllers;

import com.example.mealservice.dtos.MealDTO;
import com.example.mealservice.enums.MealType;
import com.example.mealservice.services.MealService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meals")
public class MealController {
    
    private final MealService mealService;
    
    public MealController(MealService mealService) {
        this.mealService = mealService;
    }
    
    @PostMapping
    public ResponseEntity<MealDTO> createMeal(@Valid @RequestBody MealDTO mealDTO) {
        MealDTO createdMeal = mealService.createMeal(mealDTO);
        return new ResponseEntity<>(createdMeal, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MealDTO> getMealById(@PathVariable Long id) {
        MealDTO meal = mealService.getMealById(id);
        return ResponseEntity.ok(meal);
    }
    //giới hạn quyền
    @GetMapping
    public ResponseEntity<List<MealDTO>> getAllMeals() {
        List<MealDTO> meals = mealService.getAllMeals();
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<MealDTO>> getMealsByType(@PathVariable MealType type) {
        List<MealDTO> meals = mealService.getMealsByType(type);
        return ResponseEntity.ok(meals);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MealDTO> updateMeal(@PathVariable Long id,
                                              @Valid @RequestBody MealDTO mealDTO) {
        MealDTO updatedMeal = mealService.updateMeal(id, mealDTO);
        return ResponseEntity.ok(updatedMeal);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return ResponseEntity.noContent().build();
    }
}