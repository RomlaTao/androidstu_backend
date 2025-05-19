package com.example.mealservice.mappers;

import com.example.mealservice.dtos.FoodDTO;
import com.example.mealservice.entities.Food;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FoodMapper {
    
    public FoodDTO toDTO(Food food) {
        if (food == null) {
            return null;
        }
        
        FoodDTO dto = new FoodDTO();
        dto.setId(food.getId());
        dto.setName(food.getName());
        dto.setDescription(food.getDescription());
        dto.setCarb(food.getCarb());
        dto.setProtein(food.getProtein());
        dto.setLipid(food.getLipid());
        return dto;
    }
    
    public Food toEntity(FoodDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Food food = new Food();
        updateEntityFromDTO(dto, food);
        return food;
    }
    
    public void updateEntityFromDTO(FoodDTO dto, Food food) {
        food.setName(dto.getName());
        food.setDescription(dto.getDescription());
        food.setCarb(dto.getCarb());
        food.setProtein(dto.getProtein());
        food.setLipid(dto.getLipid());
    }
    
    public List<FoodDTO> toDTOList(List<Food> foods) {
        return foods.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
} 