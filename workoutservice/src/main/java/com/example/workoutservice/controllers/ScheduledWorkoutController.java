package com.example.workoutservice.controllers;

import com.example.workoutservice.dtos.ScheduledWorkoutDTO;
import com.example.workoutservice.enums.WorkoutStatus;
import com.example.workoutservice.services.ScheduledWorkoutService;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts/scheduled-workouts")
public class ScheduledWorkoutController {

    private final ScheduledWorkoutService scheduledWorkoutService;

    public ScheduledWorkoutController(ScheduledWorkoutService scheduledWorkoutService) {
        this.scheduledWorkoutService = scheduledWorkoutService;
    }

    @PostMapping
    public ResponseEntity<ScheduledWorkoutDTO> createScheduledWorkout(
            @Valid @RequestBody ScheduledWorkoutDTO scheduledWorkoutDTO) {
        return new ResponseEntity<>(
                scheduledWorkoutService.createScheduledWorkout(scheduledWorkoutDTO),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduledWorkoutDTO> getScheduledWorkoutById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduledWorkoutService.getScheduledWorkoutById(id));
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<ScheduledWorkoutDTO>> getScheduledWorkoutsByScheduleId(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduledWorkoutService.getScheduledWorkoutsByScheduleId(scheduleId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ScheduledWorkoutDTO>> getUserWorkoutsInDateRange(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        return ResponseEntity.ok(
                scheduledWorkoutService.getUserWorkoutsInDateRange(userId, startDateTime, endDateTime)
        );
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<ScheduledWorkoutDTO>> getUserWorkoutsInDateRangeWithStatus(
            @PathVariable String userId,
            @PathVariable WorkoutStatus status,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        return ResponseEntity.ok(
                scheduledWorkoutService.getUserWorkoutsInDateRangeWithStatus(
                        userId, status, startDateTime, endDateTime
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduledWorkoutDTO> updateScheduledWorkout(
            @PathVariable Long id,
            @Valid @RequestBody ScheduledWorkoutDTO scheduledWorkoutDTO) {
        return ResponseEntity.ok(scheduledWorkoutService.updateScheduledWorkout(id, scheduledWorkoutDTO));
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<ScheduledWorkoutDTO> updateWorkoutStatus(
            @PathVariable Long id,
            @PathVariable WorkoutStatus status) {
        return ResponseEntity.ok(scheduledWorkoutService.updateWorkoutStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduledWorkout(@PathVariable Long id) {
        scheduledWorkoutService.deleteScheduledWorkout(id);
        return ResponseEntity.noContent().build();
    }
}