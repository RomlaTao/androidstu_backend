package com.example.analystservice.services.impls;

import com.example.analystservice.clients.WorkoutServiceClient;
import com.example.analystservice.dtos.WorkoutActivityDto;
import com.example.analystservice.dtos.ActivityAnalysisDto;
import com.example.analystservice.enums.ActivityLevel;
import com.example.analystservice.services.ActivityFactorCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivityFactorCalculationServiceImpl implements ActivityFactorCalculationService {

    @Autowired
    private WorkoutServiceClient workoutServiceClient;

    /**
     * Tính toán Activity Factor dựa trên số buổi tập trong 7 ngày qua
     */
    @Override
    public ActivityAnalysisDto calculateActivityFactorFromWorkouts(String userId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);

        // Lấy dữ liệu workout đã hoàn thành
        List<WorkoutActivityDto> completedWorkouts = workoutServiceClient
                .getUserWorkoutActivities(userId, startDate, endDate);

        // Tính số buổi tập trong tuần
        int weeklyWorkoutSessions = calculateWorkoutSessions(completedWorkouts);
        
        // Tính Activity Factor dựa trên số buổi tập
        double calculatedActivityFactor = calculateActivityFactorFromSessions(weeklyWorkoutSessions);
        
        // Xác định mức độ hoạt động
        ActivityLevel activityLevel = determineActivityLevel(calculatedActivityFactor);

        return ActivityAnalysisDto.builder()
                .userId(userId)
                .analysisStartDate(startDate)
                .analysisEndDate(endDate)
                .totalWorkoutDays(weeklyWorkoutSessions)
                .calculatedActivityFactor(Math.round(calculatedActivityFactor * 100.0) / 100.0)
                .activityLevelDescription(activityLevel.getDisplayName())
                .build();
    }

    /**
     * Tính số buổi tập trong tuần (không trùng ngày)
     */
    private int calculateWorkoutSessions(List<WorkoutActivityDto> workouts) {
        Set<LocalDate> workoutDates = workouts.stream()
                .map(w -> w.getScheduledDateTime().toLocalDate())
                .collect(Collectors.toSet());
        return workoutDates.size();
    }

    /**
     * Tính Activity Factor dựa trên số buổi tập/tuần
     * 0 buổi: 1.2 (Sedentary)
     * 1-2 buổi: 1.375 (Lightly Active) 
     * 3-4 buổi: 1.55 (Moderately Active)
     * 5-6 buổi: 1.725 (Very Active)
     * 7+ buổi: 1.9 (Extra Active)
     */
    private double calculateActivityFactorFromSessions(int sessions) {
        if (sessions == 0) return 1.2;
        if (sessions <= 2) return 1.375;
        if (sessions <= 4) return 1.55;
        if (sessions <= 6) return 1.725;
        return 1.9;
    }

    /**
     * Xác định mức độ hoạt động dựa trên Activity Factor
     */
    private ActivityLevel determineActivityLevel(double activityFactor) {
        if (activityFactor <= 1.2) return ActivityLevel.SEDENTARY;
        if (activityFactor <= 1.375) return ActivityLevel.LIGHTLY_ACTIVE;
        if (activityFactor <= 1.55) return ActivityLevel.MODERATELY_ACTIVE;
        if (activityFactor <= 1.725) return ActivityLevel.VERY_ACTIVE;
        return ActivityLevel.EXTRA_ACTIVE;
    }
} 