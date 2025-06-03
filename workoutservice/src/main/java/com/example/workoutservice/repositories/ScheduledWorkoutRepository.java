package com.example.workoutservice.repositories;

import com.example.workoutservice.entities.ScheduledWorkout;
import com.example.workoutservice.enums.WorkoutStatus;
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
    List<ScheduledWorkout> findUserWorkoutsInDateRange(String userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    @Query("SELECT sw FROM ScheduledWorkout sw JOIN sw.schedule s WHERE s.userId = :userId AND " +
           "sw.status = :status AND sw.scheduledDateTime BETWEEN :startDateTime AND :endDateTime " +
           "ORDER BY sw.scheduledDateTime ASC")
    List<ScheduledWorkout> findUserWorkoutsInDateRangeWithStatus(String userId, WorkoutStatus status, 
                                                             LocalDateTime startDateTime, LocalDateTime endDateTime);

    // === CALORIE BURN CALCULATION METHODS ===

    @Query("SELECT COALESCE(SUM(sw.workout.caloriesBurned), 0) FROM ScheduledWorkout sw JOIN sw.schedule s WHERE s.userId = :userId AND " +
           "sw.status = :status AND sw.scheduledDateTime >= :startOfDay AND sw.scheduledDateTime < :endOfDay")
    Integer calculateDailyCaloriesBurned(String userId, LocalDateTime startOfDay, LocalDateTime endOfDay, WorkoutStatus status);

    // Optimized batch query for date range calorie calculation
    @Query("SELECT DATE(sw.scheduledDateTime), COALESCE(SUM(sw.workout.caloriesBurned), 0) " +
           "FROM ScheduledWorkout sw JOIN sw.schedule s " +
           "WHERE s.userId = :userId AND sw.status = :status " +
           "AND sw.scheduledDateTime >= :startDateTime AND sw.scheduledDateTime < :endDateTime " +
           "GROUP BY DATE(sw.scheduledDateTime) " +
           "ORDER BY DATE(sw.scheduledDateTime)")
    List<Object[]> calculateCaloriesBurnedInDateRange(String userId, LocalDateTime startDateTime, LocalDateTime endDateTime, WorkoutStatus status);
} 