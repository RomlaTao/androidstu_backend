package com.example.mealservice.repositories;

import com.example.mealservice.entities.ScheduledMeal;
import com.example.mealservice.enums.MealStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledMealRepository extends JpaRepository<ScheduledMeal, Long> {
    List<ScheduledMeal> findByScheduleId(Long scheduleId);
    
    List<ScheduledMeal> findByStatus(MealStatus status);
    
    @Query("SELECT sw FROM ScheduledMeal sw JOIN sw.schedule s WHERE s.userId = :userId AND " +
           "sw.scheduledDateTime BETWEEN :startDateTime AND :endDateTime ORDER BY sw.scheduledDateTime ASC")
    List<ScheduledMeal> findUserMealsInDateRange(String userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    @Query("SELECT sw FROM ScheduledMeal sw JOIN sw.schedule s WHERE s.userId = :userId AND " +
           "sw.status = :status AND sw.scheduledDateTime BETWEEN :startDateTime AND :endDateTime " +
           "ORDER BY sw.scheduledDateTime ASC")
    List<ScheduledMeal> findUserMealsInDateRangeWithStatus(String userId, MealStatus status,
                                                             LocalDateTime startDateTime, LocalDateTime endDateTime);

    // === CALORIE CALCULATION METHODS ===

    @Query("SELECT COALESCE(SUM(sw.meal.calories), 0) FROM ScheduledMeal sw JOIN sw.schedule s WHERE s.userId = :userId AND " +
           "sw.status = :status AND sw.scheduledDateTime >= :startOfDay AND sw.scheduledDateTime < :endOfDay")
    Integer calculateDailyCalories(String userId, LocalDateTime startOfDay, LocalDateTime endOfDay, MealStatus status);

    @Query("SELECT COALESCE(SUM(sw.meal.calories), 0) FROM ScheduledMeal sw JOIN sw.schedule s WHERE s.userId = :userId AND " +
           "sw.status = :status AND sw.scheduledDateTime BETWEEN :startDateTime AND :endDateTime")
    Integer calculateTotalCaloriesInRange(String userId, LocalDateTime startDateTime, LocalDateTime endDateTime, MealStatus status);
} 