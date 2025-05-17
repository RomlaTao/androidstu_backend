package com.example.workoutservice.mappers;

import com.example.workoutservice.dtos.ExerciseDTO;
import com.example.workoutservice.dtos.WorkoutDTO;
import com.example.workoutservice.entities.Exercise;
import com.example.workoutservice.entities.Workout;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorkoutMapper {
    
    private final ExerciseMapper exerciseMapper;
    
    public WorkoutMapper(ExerciseMapper exerciseMapper) {
        this.exerciseMapper = exerciseMapper;
    }
    
    public WorkoutDTO toDTO(Workout workout) {
        if (workout == null) {
            return null;
        }
        
        WorkoutDTO dto = new WorkoutDTO();
        dto.setId(workout.getId());
        dto.setName(workout.getName());
        dto.setDescription(workout.getDescription());
        dto.setDurationMinutes(workout.getDurationMinutes());
        dto.setCaloriesBurned(workout.getCaloriesBurned());
        dto.setType(workout.getType());
        
        if (workout.getExercises() != null && !workout.getExercises().isEmpty()) {
            List<ExerciseDTO> exerciseDTOs = workout.getExercises().stream()
                    .map(exerciseMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setExercises(exerciseDTOs);
        }
        
        return dto;
    }
    
    public Workout toEntity(WorkoutDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Workout workout = new Workout();
        updateEntityFromDTO(dto, workout);
        return workout;
    }
    
    public void updateEntityFromDTO(WorkoutDTO dto, Workout workout) {
        workout.setName(dto.getName());
        workout.setDescription(dto.getDescription());
        workout.setDurationMinutes(dto.getDurationMinutes());
        workout.setCaloriesBurned(dto.getCaloriesBurned());
        workout.setType(dto.getType());
        
        // Cập nhật danh sách exercises
        if (dto.getExercises() != null) {
            // Xóa tất cả exercises hiện tại
            workout.getExercises().clear();
            
            // Thêm exercises mới từ DTO
            dto.getExercises().forEach(exerciseDTO -> {
                Exercise exercise = exerciseMapper.toEntity(exerciseDTO);
                exercise.setWorkout(workout);
                workout.getExercises().add(exercise);
            });
        }
    }
    
    public List<WorkoutDTO> toDTOList(List<Workout> workouts) {
        return workouts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
} 