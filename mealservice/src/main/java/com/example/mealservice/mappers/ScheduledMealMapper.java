package com.example.mealservice.mappers;

import com.example.mealservice.dtos.ScheduledMealDTO;
import com.example.mealservice.entities.ScheduledMeal;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduledMealMapper {
    
    private final MealMapper mealMapper;
    
    public ScheduledMealMapper(MealMapper mealMapper) {
        this.mealMapper = mealMapper;
    }
    
    public ScheduledMealDTO toDTO(ScheduledMeal scheduledMeal) {
        if (scheduledMeal == null) {
            return null;
        }
        
        ScheduledMealDTO dto = new ScheduledMealDTO();
        dto.setId(scheduledMeal.getId());
        
        if (scheduledMeal.getSchedule() != null) {
            dto.setScheduleId(scheduledMeal.getSchedule().getId());
        }
        
        if (scheduledMeal.getMeal() != null) {
            dto.setMealId(scheduledMeal.getMeal().getId());
            dto.setMeal(mealMapper.toDTO(scheduledMeal.getMeal()));
        }
        
        dto.setScheduledDateTime(scheduledMeal.getScheduledDateTime());
        dto.setStatus(scheduledMeal.getStatus());
        dto.setNotes(scheduledMeal.getNotes());
        
        return dto;
    }
    
    public List<ScheduledMealDTO> toDTOList(List<ScheduledMeal> scheduledMeals) {
        return scheduledMeals.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
} 