package com.example.mealservice.services.impl;

import com.example.mealservice.dtos.MealDTO;
import com.example.mealservice.entities.Meal;
import com.example.mealservice.enums.MealType;
import com.example.mealservice.exceptions.ResourceNotFoundException;
import com.example.mealservice.mappers.MealMapper;
import com.example.mealservice.repositories.MealRepository;
import com.example.mealservice.services.CalorieValidationService;
import com.example.mealservice.services.MealService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MealServiceImpl implements MealService {
    
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;
    private final CalorieValidationService calorieValidationService;
    
    public MealServiceImpl(MealRepository mealRepository, MealMapper mealMapper, 
                          CalorieValidationService calorieValidationService) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
        this.calorieValidationService = calorieValidationService;
    }
    
    @Override
    @Transactional
    public MealDTO createMeal(MealDTO mealDTO) {
        Meal meal = mealMapper.toEntity(mealDTO);
        
        // Đảm bảo calories đồng bộ trước khi lưu
        calorieValidationService.synchronizeMealCalories(meal);
        
        Meal savedMeal = mealRepository.save(meal);
        savedMeal = mealRepository.findByIdWithFoods(savedMeal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found after saving"));
        return mealMapper.toDTO(savedMeal);
    }
    
    @Override
    @Transactional(readOnly = true)
    public MealDTO getMealById(Long id) {
        Meal meal = mealRepository.findByIdWithFoods(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + id));
        return mealMapper.toDTO(meal);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MealDTO> getAllMeals() {
        List<Meal> meals = mealRepository.findAllWithFoods();
        return mealMapper.toDTOList(meals);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MealDTO> getMealsByType(MealType type) {
        List<Meal> meals = mealRepository.findByType(type);
        return mealMapper.toDTOList(meals);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MealDTO> searchMealsByName(String name) {
        List<Meal> meals = mealRepository.findByNameContainingIgnoreCase(name);
        return mealMapper.toDTOList(meals);
    }
    
    @Override
    @Transactional
    public MealDTO updateMeal(Long id, MealDTO mealDTO) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + id));
        
        mealMapper.updateEntityFromDTO(mealDTO, meal);
        
        // Đảm bảo calories đồng bộ trước khi lưu
        calorieValidationService.synchronizeMealCalories(meal);
        
        Meal updatedMeal = mealRepository.save(meal);
        updatedMeal = mealRepository.findByIdWithFoods(updatedMeal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found after updating"));
        return mealMapper.toDTO(updatedMeal);
    }
    
    @Override
    @Transactional
    public void deleteMeal(Long id) {
        if (!mealRepository.existsById(id)) {
            throw new ResourceNotFoundException("Meal not found with id: " + id);
        }
        mealRepository.deleteById(id);
    }
} 