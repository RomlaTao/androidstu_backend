package com.example.workoutservice.services;

import com.example.workoutservice.dtos.ScheduledWorkoutDTO;
import com.example.workoutservice.enums.WorkoutStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledWorkoutService {
    
    ScheduledWorkoutDTO createScheduledWorkout(ScheduledWorkoutDTO scheduledWorkoutDTO);
    
    ScheduledWorkoutDTO getScheduledWorkoutById(Long id);
    
    List<ScheduledWorkoutDTO> getScheduledWorkoutsByScheduleId(Long scheduleId);
    
    List<ScheduledWorkoutDTO> getUserWorkoutsInDateRange(String userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    List<ScheduledWorkoutDTO> getUserWorkoutsInDateRangeWithStatus(String userId, WorkoutStatus status, 
                                               LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    ScheduledWorkoutDTO updateScheduledWorkout(Long id, ScheduledWorkoutDTO scheduledWorkoutDTO);
    
    ScheduledWorkoutDTO updateWorkoutStatus(Long id, WorkoutStatus status);
    
    void deleteScheduledWorkout(Long id);
} 