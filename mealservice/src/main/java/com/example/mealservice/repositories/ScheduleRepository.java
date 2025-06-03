package com.example.mealservice.repositories;

import com.example.mealservice.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserId(String userId);
    List<Schedule> findByUserIdAndStartDateGreaterThanEqual(String userId, LocalDate date);
    List<Schedule> findByUserIdAndEndDateGreaterThanEqualAndStartDateLessThanEqual(String userId, LocalDate startDate, LocalDate endDate);
} 