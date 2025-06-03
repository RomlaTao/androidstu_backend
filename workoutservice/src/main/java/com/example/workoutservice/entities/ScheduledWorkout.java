package com.example.workoutservice.entities;

import com.example.workoutservice.enums.WorkoutStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_workouts")
public class ScheduledWorkout {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    
    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;
    
    @NotNull
    private LocalDateTime scheduledDateTime;
    
    @Enumerated(EnumType.STRING)
    private WorkoutStatus status;
    
    private String notes;
    
    public ScheduledWorkout() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Schedule getSchedule() {
        return schedule;
    }
    
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
    
    public Workout getWorkout() {
        return workout;
    }
    
    public void setWorkout(Workout workout) {
        this.workout = workout;
    }
    
    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }
    
    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }
    
    public WorkoutStatus getStatus() {
        return status;
    }
    
    public void setStatus(WorkoutStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
} 