package com.example.workoutservice.repositories;

import com.example.workoutservice.entities.Workout;
import com.example.workoutservice.entities.WorkoutType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByType(WorkoutType type);
    List<Workout> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT w FROM Workout w LEFT JOIN FETCH w.exercises WHERE w.id = :id")
    Optional<Workout> findByIdWithExercises(@Param("id") Long id);
    
    @Query("SELECT w FROM Workout w LEFT JOIN FETCH w.exercises")
    List<Workout> findAllWithExercises();
} 