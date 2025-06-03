package com.example.analystservice.strategies;

import com.example.analystservice.clients.WorkoutServiceClient;
import com.example.analystservice.dtos.UserDto;
import com.example.analystservice.dtos.TdeeCalculationDto;
import com.example.analystservice.enums.ActivityLevel;
import com.example.analystservice.services.HealthCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class TdeeCalculationStrategy {

    @Autowired
    private HealthCalculationService healthCalculationService;

    @Autowired
    private WorkoutServiceClient workoutServiceClient;

    /**
     * Quyết định phương thức tính TDEE dựa trên logic:
     * - Nếu user mới (< 7 ngày) hoặc chưa có workout data: dùng manual activity level
     * - Nếu user đã dùng app >= 7 ngày và có workout data: dùng workout-based calculation
     */
    public TdeeCalculationDto calculateTdeeWithStrategy(UserDto user) {
        TdeeCalculationMethod method = determineTdeeCalculationMethod(user);
        
        switch (method) {
            case MANUAL_ACTIVITY_LEVEL:
                return calculateWithManualActivityLevel(user);
            case WORKOUT_BASED:
                return calculateWithWorkoutData(user);
            default:
                throw new IllegalStateException("Unknown TDEE calculation method: " + method);
        }
    }

    /**
     * Xác định phương thức tính TDEE
     */
    private TdeeCalculationMethod determineTdeeCalculationMethod(UserDto user) {
        // Kiểm tra user đã dùng app bao lâu
        boolean isNewUser = isUserNew(user);
        
        if (isNewUser) {
            return TdeeCalculationMethod.MANUAL_ACTIVITY_LEVEL;
        }

        // Kiểm tra có workout data trong 7 ngày qua không
        boolean hasWorkoutData = hasRecentWorkoutData(user.getId());
        
        if (hasWorkoutData) {
            return TdeeCalculationMethod.WORKOUT_BASED;
        } else {
            // Fallback về manual nếu không có workout data
            return TdeeCalculationMethod.MANUAL_ACTIVITY_LEVEL;
        }
    }

    /**
     * Kiểm tra user có phải là user mới (< 7 ngày)
     */
    private boolean isUserNew(UserDto user) {
        if (user.getCreatedAt() == null) {
            return true; // Coi như user mới nếu không có thông tin
        }

        LocalDate userCreatedDate = user.getCreatedAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        
        return userCreatedDate.isAfter(sevenDaysAgo);
    }

    /**
     * Kiểm tra có workout data trong 7 ngày qua
     */
    private boolean hasRecentWorkoutData(String userId) {
        try {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(7);
            
            var workouts = workoutServiceClient.getUserWorkoutActivities(userId, startDate, endDate);
            return workouts != null && !workouts.isEmpty();
        } catch (Exception e) {
            // Nếu có lỗi khi gọi workout service, fallback về manual
            return false;
        }
    }

    /**
     * Tính TDEE với manual activity level
     */
    private TdeeCalculationDto calculateWithManualActivityLevel(UserDto user) {
        // Sử dụng activity level mà user đã chọn khi đăng ký
        ActivityLevel activityLevel = convertToActivityLevel(user.getInitialActivityLevel());
        
        TdeeCalculationDto result = healthCalculationService.createTdeeCalculation(user, activityLevel);
        result.setCalculationMethod("Manual Activity Level");
        result.setMethodDescription("Dựa trên mức độ hoạt động bạn đã chọn khi đăng ký");
        
        return result;
    }

    /**
     * Tính TDEE với workout data
     */
    private TdeeCalculationDto calculateWithWorkoutData(UserDto user) {
        TdeeCalculationDto result = healthCalculationService.createWorkoutBasedTdeeCalculation(user);
        result.setCalculationMethod("Workout-based Analysis");
        result.setMethodDescription("Dựa trên lịch tập thực tế của bạn trong 7 ngày qua");
        
        return result;
    }

    /**
     * Convert InitialActivityLevel sang ActivityLevel
     */
    private ActivityLevel convertToActivityLevel(String initialActivityLevel) {
        if (initialActivityLevel == null) {
            return ActivityLevel.SEDENTARY; // Default
        }
        
        try {
            return ActivityLevel.valueOf(initialActivityLevel);
        } catch (IllegalArgumentException e) {
            return ActivityLevel.SEDENTARY; // Default nếu không match
        }
    }

    /**
     * Enum định nghĩa các phương thức tính TDEE
     */
    public enum TdeeCalculationMethod {
        MANUAL_ACTIVITY_LEVEL,
        WORKOUT_BASED
    }
} 