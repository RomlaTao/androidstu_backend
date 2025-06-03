package com.example.workoutservice.services;

import com.example.workoutservice.dtos.WorkoutDTO;
import com.example.workoutservice.enums.WorkoutType;

import java.util.List;

public interface WorkoutService {
    
    WorkoutDTO createWorkout(WorkoutDTO workoutDTO);
    
    WorkoutDTO getWorkoutById(Long id);
    
    List<WorkoutDTO> getAllWorkouts();
    
    List<WorkoutDTO> getWorkoutsByType(WorkoutType type);
    
    List<WorkoutDTO> searchWorkoutsByName(String name);
    
    WorkoutDTO updateWorkout(Long id, WorkoutDTO workoutDTO);
    
    void deleteWorkout(Long id);
} 