package com.example.workoutservice.mappers;

import com.example.workoutservice.dtos.ScheduledWorkoutDTO;
import com.example.workoutservice.entities.ScheduledWorkout;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduledWorkoutMapper {
    
    private final WorkoutMapper workoutMapper;
    
    public ScheduledWorkoutMapper(WorkoutMapper workoutMapper) {
        this.workoutMapper = workoutMapper;
    }
    
    public ScheduledWorkoutDTO toDTO(ScheduledWorkout scheduledWorkout) {
        if (scheduledWorkout == null) {
            return null;
        }
        
        ScheduledWorkoutDTO dto = new ScheduledWorkoutDTO();
        dto.setId(scheduledWorkout.getId());
        
        if (scheduledWorkout.getSchedule() != null) {
            dto.setScheduleId(scheduledWorkout.getSchedule().getId());
        }
        
        if (scheduledWorkout.getWorkout() != null) {
            dto.setWorkoutId(scheduledWorkout.getWorkout().getId());
            dto.setWorkout(workoutMapper.toDTO(scheduledWorkout.getWorkout()));
        }
        
        dto.setScheduledDateTime(scheduledWorkout.getScheduledDateTime());
        dto.setStatus(scheduledWorkout.getStatus());
        dto.setNotes(scheduledWorkout.getNotes());
        
        return dto;
    }
    
    public List<ScheduledWorkoutDTO> toDTOList(List<ScheduledWorkout> scheduledWorkouts) {
        return scheduledWorkouts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
} 