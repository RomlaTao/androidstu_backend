package com.example.workoutservice.controllers;

import com.example.workoutservice.dtos.WorkoutDTO;
import com.example.workoutservice.entities.WorkoutType;
import com.example.workoutservice.services.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts")
public class WorkoutController {
    
    private final WorkoutService workoutService;
    
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }
    
    @PostMapping
    public ResponseEntity<WorkoutDTO> createWorkout(@Valid @RequestBody WorkoutDTO workoutDTO) {
        WorkoutDTO createdWorkout = workoutService.createWorkout(workoutDTO);
        return new ResponseEntity<>(createdWorkout, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDTO> getWorkoutById(@PathVariable Long id) {
        WorkoutDTO workout = workoutService.getWorkoutById(id);
        return ResponseEntity.ok(workout);
    }
    
    @GetMapping
    public ResponseEntity<List<WorkoutDTO>> getAllWorkouts() {
        List<WorkoutDTO> workouts = workoutService.getAllWorkouts();
        return ResponseEntity.ok(workouts);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<WorkoutDTO>> getWorkoutsByType(@PathVariable WorkoutType type) {
        List<WorkoutDTO> workouts = workoutService.getWorkoutsByType(type);
        return ResponseEntity.ok(workouts);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<WorkoutDTO>> searchWorkoutsByName(@RequestParam String name) {
        List<WorkoutDTO> workouts = workoutService.searchWorkoutsByName(name);
        return ResponseEntity.ok(workouts);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<WorkoutDTO> updateWorkout(@PathVariable Long id, 
                                              @Valid @RequestBody WorkoutDTO workoutDTO) {
        WorkoutDTO updatedWorkout = workoutService.updateWorkout(id, workoutDTO);
        return ResponseEntity.ok(updatedWorkout);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }
} 