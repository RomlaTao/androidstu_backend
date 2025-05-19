package com.example.mealservice.mappers;

import com.example.mealservice.dtos.ScheduleDTO;
import com.example.mealservice.dtos.ScheduledMealDTO;
import com.example.mealservice.entities.Schedule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduleMapper {
    
    private final ScheduledMealMapper scheduledMealMapper;
    
    public ScheduleMapper(ScheduledMealMapper scheduledMealMapper) {
        this.scheduledMealMapper = scheduledMealMapper;
    }
    
    public ScheduleDTO toDTO(Schedule schedule) {
        if (schedule == null) {
            return null;
        }
        
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(schedule.getId());
        dto.setUserId(schedule.getUserId());
        dto.setName(schedule.getName());
        dto.setDescription(schedule.getDescription());
        dto.setStartDate(schedule.getStartDate());
        dto.setEndDate(schedule.getEndDate());
        
        if (schedule.getScheduledMeals() != null && !schedule.getScheduledMeals().isEmpty()) {
            List<ScheduledMealDTO> mealDTOs = schedule.getScheduledMeals().stream()
                    .map(scheduledMealMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setScheduledMeals(mealDTOs);
        }
        
        return dto;
    }
    
    public Schedule toEntity(ScheduleDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Schedule schedule = new Schedule();
        updateEntityFromDTO(dto, schedule);
        return schedule;
    }
    
    public void updateEntityFromDTO(ScheduleDTO dto, Schedule schedule) {
        schedule.setUserId(dto.getUserId());
        schedule.setName(dto.getName());
        schedule.setDescription(dto.getDescription());
        schedule.setStartDate(dto.getStartDate());
        schedule.setEndDate(dto.getEndDate());
    }
    
    public List<ScheduleDTO> toDTOList(List<Schedule> schedules) {
        return schedules.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
} 