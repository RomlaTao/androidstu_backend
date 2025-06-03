package com.example.workoutservice.services.impl;

import com.example.workoutservice.dtos.WorkoutDTO;
import com.example.workoutservice.exceptions.ResourceNotFoundException;
import com.example.workoutservice.mappers.WorkoutMapper;
import com.example.workoutservice.entities.Workout;
import com.example.workoutservice.enums.WorkoutType;
import com.example.workoutservice.repositories.WorkoutRepository;
import com.example.workoutservice.services.WorkoutService;
import com.example.workoutservice.services.MetabolicCalculationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {
    
    private static final Logger logger = LoggerFactory.getLogger(WorkoutServiceImpl.class);
    
    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;
    private final MetabolicCalculationService metabolicCalculationService;
    
    public WorkoutServiceImpl(WorkoutRepository workoutRepository, 
                             WorkoutMapper workoutMapper,
                             MetabolicCalculationService metabolicCalculationService) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
        this.metabolicCalculationService = metabolicCalculationService;
    }
    
    @Override
    @Transactional
    public WorkoutDTO createWorkout(WorkoutDTO workoutDTO) {
        return createWorkoutForUser(workoutDTO, null);
    }
    
    /**
     * Create workout với userId để tính calories chính xác
     */
    @Transactional
    public WorkoutDTO createWorkoutForUser(WorkoutDTO workoutDTO, Long userId) {
        Workout workout = workoutMapper.toEntity(workoutDTO);
        
        // Auto-calculate calories using user's actual weight
        if (userId != null) {
            metabolicCalculationService.validateAndSynchronizeWorkoutCalories(workout, userId);
            logger.info("Created workout with user-specific calorie calculation for userId: {}", userId);
        } else {
            // Fallback: sử dụng default weight calculation nếu không có userId
            metabolicCalculationService.validateAndSynchronizeWorkoutCalories(workout, null);
            logger.warn("Created workout without userId, using default weight calculation");
        }
        
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
        return updateWorkoutForUser(id, workoutDTO, null);
    }
    
    /**
     * Update workout với userId để recalculate calories chính xác
     */
    @Transactional
    public WorkoutDTO updateWorkoutForUser(Long id, WorkoutDTO workoutDTO, Long userId) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));
        
        workoutMapper.updateEntityFromDTO(workoutDTO, workout);
        
        // Auto-calculate calories after update với user's actual weight
        if (userId != null) {
            metabolicCalculationService.validateAndSynchronizeWorkoutCalories(workout, userId);
            logger.info("Updated workout with user-specific calorie recalculation for userId: {}", userId);
        } else {
            // Fallback: sử dụng default weight calculation nếu không có userId
            metabolicCalculationService.validateAndSynchronizeWorkoutCalories(workout, null);
            logger.warn("Updated workout without userId, using default weight calculation");
        }
        
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
    
    /**
     * Recalculate calories cho existing workout khi user weight thay đổi
     */
    @Transactional
    public WorkoutDTO recalculateWorkoutCalories(Long workoutId, Long userId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + workoutId));
        
        // Recalculate với updated user weight
        metabolicCalculationService.validateAndSynchronizeWorkoutCalories(workout, userId);
        
        Workout updatedWorkout = workoutRepository.save(workout);
        logger.info("Recalculated calories for workout: {} (userId: {}), new calories: {}", 
                   workoutId, userId, updatedWorkout.getCaloriesBurned());
        
        return workoutMapper.toDTO(updatedWorkout);
    }
} 