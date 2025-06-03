package com.example.mealservice.mappers;

import com.example.mealservice.dtos.FoodDTO;
import com.example.mealservice.dtos.MealDTO;
import com.example.mealservice.entities.Food;
import com.example.mealservice.entities.Meal;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MealMapper {
    
    private final FoodMapper foodMapper;
    
    public MealMapper(FoodMapper foodMapper) {
        this.foodMapper = foodMapper;
    }
    
    public MealDTO toDTO(Meal meal) {
        if (meal == null) {
            return null;
        }
        
        MealDTO dto = new MealDTO();
        dto.setId(meal.getId());
        dto.setName(meal.getName());
        dto.setDescription(meal.getDescription());
        dto.setCalories(meal.getCalories());
        dto.setType(meal.getType());
        
        if (meal.getFoods() != null && !meal.getFoods().isEmpty()) {
            List<FoodDTO> foodDTOs = meal.getFoods().stream()
                    .map(foodMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setFoods(foodDTOs);
        }
        
        return dto;
    }
    
    public Meal toEntity(MealDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Meal meal = new Meal();
        updateEntityFromDTO(dto, meal);
        return meal;
    }
    
    public void updateEntityFromDTO(MealDTO dto, Meal meal) {
        meal.setName(dto.getName());
        meal.setDescription(dto.getDescription());
        meal.setType(dto.getType());
        
        // Cập nhật danh sách foods trước
        if (dto.getFoods() != null) {
            // Xóa tất cả foods hiện tại
            meal.getFoods().clear();
            
            // Thêm foods mới từ DTO
            dto.getFoods().forEach(foodDTO -> {
                Food food = foodMapper.toEntity(foodDTO);
                food.setMeal(meal);
                meal.getFoods().add(food);
            });
        }
        
        // Tự động tính calories từ foods
        // Nếu DTO có calories và khác với tổng foods, ưu tiên foods
        meal.updateCaloriesFromFoods();
        
        // Nếu không có foods nào và DTO có calories thì sử dụng calories từ DTO
        if ((meal.getFoods() == null || meal.getFoods().isEmpty()) && dto.getCalories() != null) {
            meal.setCalories(dto.getCalories());
        }
    }
    
    public List<MealDTO> toDTOList(List<Meal> meals) {
        return meals.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
} 