package com.example.workoutservice.services.impl;

import com.example.workoutservice.dtos.WorkoutDTO;
import com.example.workoutservice.exceptions.ResourceNotFoundException;
import com.example.workoutservice.mappers.WorkoutMapper;
import com.example.workoutservice.entities.Workout;
import com.example.workoutservice.entities.WorkoutType;
import com.example.workoutservice.repositories.WorkoutRepository;
import com.example.workoutservice.services.WorkoutService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    
    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;
    
    public WorkoutServiceImpl(WorkoutRepository workoutRepository, WorkoutMapper workoutMapper) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
    }
    
    @Override
    @Transactional
    public WorkoutDTO createWorkout(WorkoutDTO workoutDTO) {
        Workout workout = workoutMapper.toEntity(workoutDTO);
        Workout savedWorkout = workoutRepository.save(workout);
        savedWorkout = workoutRepository.findByIdWithExercises(savedWorkout.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found after saving"));
        return workoutMapper.toDTO(savedWorkout);
    }
    
    @Override
    @Transactional(readOnly = true)
    public WorkoutDTO getWorkoutById(Long id) {
        Workout workout = workoutRepository.findByIdWithExercises(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));
        return workoutMapper.toDTO(workout);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkoutDTO> getAllWorkouts() {
        List<Workout> workouts = workoutRepository.findAllWithExercises();
        return workoutMapper.toDTOList(workouts);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkoutDTO> getWorkoutsByType(WorkoutType type) {
        List<Workout> workouts = workoutRepository.findByType(type);
        return workoutMapper.toDTOList(workouts);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkoutDTO> searchWorkoutsByName(String name) {
        List<Workout> workouts = workoutRepository.findByNameContainingIgnoreCase(name);
        return workoutMapper.toDTOList(workouts);
    }
    
    @Override
    @Transactional
    public WorkoutDTO updateWorkout(Long id, WorkoutDTO workoutDTO) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));
        
        workoutMapper.updateEntityFromDTO(workoutDTO, workout);
        Workout updatedWorkout = workoutRepository.save(workout);
        updatedWorkout = workoutRepository.findByIdWithExercises(updatedWorkout.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found after updating"));
        return workoutMapper.toDTO(updatedWorkout);
    }
    
    @Override
    @Transactional
    public void deleteWorkout(Long id) {
        if (!workoutRepository.existsById(id)) {
            throw new ResourceNotFoundException("Workout not found with id: " + id);
        }
        workoutRepository.deleteById(id);
    }
} 