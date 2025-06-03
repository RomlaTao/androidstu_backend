package com.example.analystservice.dtos;

import java.time.LocalDateTime;

public class WorkoutActivityDto {
    private Long id;
    private String workoutName;
    private String workoutType;
    private Integer durationMinutes;
    private Integer caloriesBurned;
    private LocalDateTime scheduledDateTime;
    private String status;
    private String userId;

    // Constructors
    public WorkoutActivityDto() {}

    public WorkoutActivityDto(Long id, String workoutName, String workoutType, Integer durationMinutes,
                             Integer caloriesBurned, LocalDateTime scheduledDateTime, String status, String userId) {
        this.id = id;
        this.workoutName = workoutName;
        this.workoutType = workoutType;
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = caloriesBurned;
        this.scheduledDateTime = scheduledDateTime;
        this.status = status;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getWorkoutName() { return workoutName; }
    public void setWorkoutName(String workoutName) { this.workoutName = workoutName; }

    public String getWorkoutType() { return workoutType; }
    public void setWorkoutType(String workoutType) { this.workoutType = workoutType; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Integer getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(Integer caloriesBurned) { this.caloriesBurned = caloriesBurned; }

    public LocalDateTime getScheduledDateTime() { return scheduledDateTime; }
    public void setScheduledDateTime(LocalDateTime scheduledDateTime) { this.scheduledDateTime = scheduledDateTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}