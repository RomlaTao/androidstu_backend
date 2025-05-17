package com.example.workoutservice.mappers;

import com.example.workoutservice.dtos.ExerciseDTO;
import com.example.workoutservice.entities.Exercise;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExerciseMapper {
    
    public ExerciseDTO toDTO(Exercise exercise) {
        if (exercise == null) {
            return null;
        }
        
        ExerciseDTO dto = new ExerciseDTO();
        dto.setId(exercise.getId());
        dto.setName(exercise.getName());
        dto.setDescription(exercise.getDescription());
        dto.setSets(exercise.getSets());
        dto.setReps(exercise.getReps());
        dto.setDurationSeconds(exercise.getDurationSeconds());
        return dto;
    }
    
    public Exercise toEntity(ExerciseDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Exercise exercise = new Exercise();
        updateEntityFromDTO(dto, exercise);
        return exercise;
    }
    
    public void updateEntityFromDTO(ExerciseDTO dto, Exercise exercise) {
        exercise.setName(dto.getName());
        exercise.setDescription(dto.getDescription());
        exercise.setSets(dto.getSets());
        exercise.setReps(dto.getReps());
        exercise.setDurationSeconds(dto.getDurationSeconds());
    }
    
    public List<ExerciseDTO> toDTOList(List<Exercise> exercises) {
        return exercises.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
} 