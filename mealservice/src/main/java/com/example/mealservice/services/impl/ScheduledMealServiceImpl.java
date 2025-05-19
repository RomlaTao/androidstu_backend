package com.example.mealservice.services.impl;

import com.example.mealservice.dtos.ScheduledMealDTO;
import com.example.mealservice.entities.ScheduledMeal;
import com.example.mealservice.entities.MealStatus;
import com.example.mealservice.exceptions.ResourceNotFoundException;
import com.example.mealservice.mappers.ScheduledMealMapper;
import com.example.mealservice.repositories.ScheduleRepository;
import com.example.mealservice.repositories.ScheduledMealRepository;
import com.example.mealservice.repositories.MealRepository;
import com.example.mealservice.services.ScheduledMealService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledMealServiceImpl implements ScheduledMealService {
    
    private final ScheduledMealRepository scheduledMealRepository;
    private final ScheduleRepository scheduleRepository;
    private final MealRepository mealRepository;
    private final ScheduledMealMapper scheduledMealMapper;
    
    public ScheduledMealServiceImpl(ScheduledMealRepository scheduledMealRepository,
                                    ScheduleRepository scheduleRepository,
                                    MealRepository mealRepository,
                                    ScheduledMealMapper scheduledMealMapper) {
        this.scheduledMealRepository = scheduledMealRepository;
        this.scheduleRepository = scheduleRepository;
        this.mealRepository = mealRepository;
        this.scheduledMealMapper = scheduledMealMapper;
    }
    
    @Override
    @Transactional
    public ScheduledMealDTO createScheduledMeal(ScheduledMealDTO scheduledMealDTO) {
        // Validate that schedule exists
        if (scheduledMealDTO.getScheduleId() != null) {
            scheduleRepository.findById(scheduledMealDTO.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + scheduledMealDTO.getScheduleId()));
        }
        
        // Validate that meal exists
        mealRepository.findById(scheduledMealDTO.getMealId())
            .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + scheduledMealDTO.getMealId()));
        
        // Set initial status if not provided
        if (scheduledMealDTO.getStatus() == null) {
            scheduledMealDTO.setStatus(MealStatus.SCHEDULED);
        }
        
        ScheduledMeal scheduledMeal = new ScheduledMeal();
        
        // Set schedule relationship
        if (scheduledMealDTO.getScheduleId() != null) {
            scheduledMeal.setSchedule(scheduleRepository.findById(scheduledMealDTO.getScheduleId()).get());
        }
        
        // Set meal relationship
        scheduledMeal.setMeal(mealRepository.findById(scheduledMealDTO.getMealId()).get());
        
        // Set other fields
        scheduledMeal.setScheduledDateTime(scheduledMealDTO.getScheduledDateTime());
        scheduledMeal.setStatus(scheduledMealDTO.getStatus());
        scheduledMeal.setNotes(scheduledMealDTO.getNotes());
        
        ScheduledMeal savedScheduledMeal = scheduledMealRepository.save(scheduledMeal);
        return scheduledMealMapper.toDTO(savedScheduledMeal);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ScheduledMealDTO getScheduledMealById(Long id) {
        ScheduledMeal scheduledMeal = scheduledMealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheduled meal not found with id: " + id));
        return scheduledMealMapper.toDTO(scheduledMeal);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ScheduledMealDTO> getScheduledMealsByScheduleId(Long scheduleId) {
        List<ScheduledMeal> scheduledMeals = scheduledMealRepository.findByScheduleId(scheduleId);
        return scheduledMealMapper.toDTOList(scheduledMeals);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ScheduledMealDTO> getUserMealsInDateRange(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<ScheduledMeal> scheduledMeals = scheduledMealRepository.findUserMealsInDateRange(
                userId, startDateTime, endDateTime);
        return scheduledMealMapper.toDTOList(scheduledMeals);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ScheduledMealDTO> getUserMealsInDateRangeWithStatus(Long userId, MealStatus status,
                                                          LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<ScheduledMeal> scheduledMeals = scheduledMealRepository.findUserMealsInDateRangeWithStatus(
                userId, status, startDateTime, endDateTime);
        return scheduledMealMapper.toDTOList(scheduledMeals);
    }
    
    @Override
    @Transactional
    public ScheduledMealDTO updateScheduledMeal(Long id, ScheduledMealDTO scheduledMealDTO) {
        ScheduledMeal scheduledMeal = scheduledMealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheduled meal not found with id: " + id));
        
        if (scheduledMealDTO.getMealId() != null &&
            !scheduledMealDTO.getMealId().equals(scheduledMeal.getMeal().getId())) {
            scheduledMeal.setMeal(mealRepository.findById(scheduledMealDTO.getMealId())
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + scheduledMealDTO.getMealId())));
        }
        
        scheduledMeal.setScheduledDateTime(scheduledMealDTO.getScheduledDateTime());
        scheduledMeal.setStatus(scheduledMealDTO.getStatus());
        scheduledMeal.setNotes(scheduledMealDTO.getNotes());
        
        ScheduledMeal updatedScheduledMeal = scheduledMealRepository.save(scheduledMeal);
        return scheduledMealMapper.toDTO(updatedScheduledMeal);
    }
    
    @Override
    @Transactional
    public ScheduledMealDTO updateMealStatus(Long id, MealStatus status) {
        ScheduledMeal scheduledMeal = scheduledMealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheduled meal not found with id: " + id));
        
        scheduledMeal.setStatus(status);
        ScheduledMeal updatedScheduledMeal = scheduledMealRepository.save(scheduledMeal);
        return scheduledMealMapper.toDTO(updatedScheduledMeal);
    }
    
    @Override
    @Transactional
    public void deleteScheduledMeal(Long id) {
        if (!scheduledMealRepository.existsById(id)) {
            throw new ResourceNotFoundException("Scheduled meal not found with id: " + id);
        }
        scheduledMealRepository.deleteById(id);
    }
} 