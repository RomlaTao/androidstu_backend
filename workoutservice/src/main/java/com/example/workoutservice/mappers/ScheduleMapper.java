package com.example.workoutservice.mappers;

import com.example.workoutservice.dtos.ScheduleDTO;
import com.example.workoutservice.dtos.ScheduledWorkoutDTO;
import com.example.workoutservice.entities.Schedule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduleMapper {
    
    private final ScheduledWorkoutMapper scheduledWorkoutMapper;
    
    public ScheduleMapper(ScheduledWorkoutMapper scheduledWorkoutMapper) {
        this.scheduledWorkoutMapper = scheduledWorkoutMapper;
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
        
        if (schedule.getScheduledWorkouts() != null && !schedule.getScheduledWorkouts().isEmpty()) {
            List<ScheduledWorkoutDTO> workoutDTOs = schedule.getScheduledWorkouts().stream()
                    .map(scheduledWorkoutMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setScheduledWorkouts(workoutDTOs);
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