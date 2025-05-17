package com.example.workoutservice.repositories;

import com.example.workoutservice.entities.ScheduledWorkout;
import com.example.workoutservice.entities.WorkoutStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledWorkoutRepository extends JpaRepository<ScheduledWorkout, Long> {
    List<ScheduledWorkout> findByScheduleId(Long scheduleId);
    
    List<ScheduledWorkout> findByStatus(WorkoutStatus status);
    
    @Query("SELECT sw FROM ScheduledWorkout sw JOIN sw.schedule s WHERE s.userId = :userId AND " +
           "sw.scheduledDateTime BETWEEN :startDateTime AND :endDateTime ORDER BY sw.scheduledDateTime ASC")
    List<ScheduledWorkout> findUserWorkoutsInDateRange(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    @Query("SELECT sw FROM ScheduledWorkout sw JOIN sw.schedule s WHERE s.userId = :userId AND " +
           "sw.status = :status AND sw.scheduledDateTime BETWEEN :startDateTime AND :endDateTime " +
           "ORDER BY sw.scheduledDateTime ASC")
    List<ScheduledWorkout> findUserWorkoutsInDateRangeWithStatus(Long userId, WorkoutStatus status, 
                                                             LocalDateTime startDateTime, LocalDateTime endDateTime);
} 