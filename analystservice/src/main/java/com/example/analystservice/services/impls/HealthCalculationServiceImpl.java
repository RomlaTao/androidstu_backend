package com.example.analystservice.services.impls;

import com.example.analystservice.dtos.UserDto;
import com.example.analystservice.dtos.HealthMetricsDto;
import com.example.analystservice.dtos.TdeeCalculationDto;
import com.example.analystservice.dtos.ActivityAnalysisDto;
import com.example.analystservice.enums.ActivityLevel;
import com.example.analystservice.enums.BmiCategory;
import com.example.analystservice.services.ActivityFactorCalculationService;
import com.example.analystservice.services.HealthCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Service
public class HealthCalculationServiceImpl implements HealthCalculationService {

    @Autowired
    private ActivityFactorCalculationService activityFactorCalculationService;

    /**
     * Calculate BMI (Body Mass Index)
     */
    @Override
    public double calculateBmi(double weightKg, double heightCm) {
        if (heightCm <= 0 || weightKg <= 0) {
            throw new IllegalArgumentException("Height and weight must be positive values");
        }
        double heightM = heightCm / 100.0;
        return weightKg / (heightM * heightM);
    }

    /**
     * Calculate BMR using Mifflin-St Jeor Equation
     */
    @Override
    public double calculateBmr(double weightKg, double heightCm, int age, String gender) {
        if (heightCm <= 0 || weightKg <= 0 || age <= 0) {
            throw new IllegalArgumentException("Height, weight, and age must be positive values");
        }

        double bmr = (10 * weightKg) + (6.25 * heightCm) - (5 * age);

        if ("MALE".equalsIgnoreCase(gender) || "M".equalsIgnoreCase(gender)) {
            bmr += 5;
        } else {
            bmr -= 161;
        }

        return bmr;
    }

    /**
     * Calculate TDEE
     */
    @Override
    public double calculateTdee(double bmr, double activityFactor) {
        return bmr * activityFactor;
    }

    /**
     * Calculate age from birth date
     */
    @Override
    public int calculateAge(java.util.Date birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }

        LocalDate birth = new java.sql.Date(birthDate.getTime()).toLocalDate();
        LocalDate now = LocalDate.now();
        return Period.between(birth, now).getYears();
    }

    /**
     * Create complete health metrics from user data
     */
    @Override
    public HealthMetricsDto createHealthMetrics(UserDto user) {
        if (user.getHeight() == null || user.getWeight() == null || user.getBirthDate() == null) {
            throw new IllegalArgumentException("User must have height, weight, and birth date to calculate health metrics");
        }

        int age = calculateAge(user.getBirthDate());
        double bmi = calculateBmi(user.getWeight(), user.getHeight());
        BmiCategory bmiCategory = BmiCategory.fromBmi(bmi);
        double bmr = calculateBmr(user.getWeight(), user.getHeight(), age, user.getGender());

        return HealthMetricsDto.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .height(user.getHeight())
                .weight(user.getWeight())
                .age(age)
                .gender(user.getGender())
                .bmi(Math.round(bmi * 100.0) / 100.0)
                .bmiCategory(bmiCategory.getDisplayName())
                .bmr(Math.round(bmr * 100.0) / 100.0)
                .bmrFormula("Mifflin-St Jeor Equation")
                .calculatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Create TDEE calculation with manual activity level
     */
    @Override
    public TdeeCalculationDto createTdeeCalculation(UserDto user, ActivityLevel activityLevel) {
        HealthMetricsDto healthMetrics = createHealthMetrics(user);
        double bmr = healthMetrics.getBmr();
        double activityFactor = activityLevel.getFactor();
        double tdee = calculateTdee(bmr, activityFactor);

        return TdeeCalculationDto.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .bmr(bmr)
                .activityLevel(activityLevel.getDisplayName())
                .activityFactor(activityFactor)
                .tdee(Math.round(tdee * 100.0) / 100.0)
                .maintenanceCalories(Math.round(tdee * 100.0) / 100.0)
                .weightLossCalories(Math.round((tdee - 500) * 100.0) / 100.0)
                .weightGainCalories(Math.round((tdee + 500) * 100.0) / 100.0)
                .calculatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Create TDEE calculation with activity factor from workout data
     */
    @Override
    public TdeeCalculationDto createWorkoutBasedTdeeCalculation(UserDto user) {
        HealthMetricsDto healthMetrics = createHealthMetrics(user);
        double bmr = healthMetrics.getBmr();

        // Tính toán Activity Factor từ dữ liệu workout thực tế
        ActivityAnalysisDto activityAnalysis = activityFactorCalculationService
                .calculateActivityFactorFromWorkouts(user.getId());
        
        double workoutBasedActivityFactor = activityAnalysis.getCalculatedActivityFactor();
        double tdee = calculateTdee(bmr, workoutBasedActivityFactor);

        return TdeeCalculationDto.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .bmr(bmr)
                .activityLevel("Workout-based (" + activityAnalysis.getActivityLevelDescription() + ")")
                .activityFactor(workoutBasedActivityFactor)
                .tdee(Math.round(tdee * 100.0) / 100.0)
                .maintenanceCalories(Math.round(tdee * 100.0) / 100.0)
                .weightLossCalories(Math.round((tdee - 500) * 100.0) / 100.0)
                .weightGainCalories(Math.round((tdee + 500) * 100.0) / 100.0)
                .calculatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Create TDEE calculation using user input TDEE value
     */
    @Override
    public TdeeCalculationDto createUserInputTdeeCalculation(UserDto user) {
        if (user.getUserInputTdee() == null) {
            throw new IllegalArgumentException("User has not provided a TDEE value yet");
        }

        HealthMetricsDto healthMetrics = createHealthMetrics(user);
        double bmr = healthMetrics.getBmr();
        double userTdee = user.getUserInputTdee();
        
        // Calculate implied activity factor from user input
        double impliedActivityFactor = userTdee / bmr;

        return TdeeCalculationDto.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .bmr(bmr)
                .activityLevel("User Input")
                .activityFactor(Math.round(impliedActivityFactor * 100.0) / 100.0)
                .tdee(userTdee)
                .maintenanceCalories(userTdee)
                .weightLossCalories(Math.round((userTdee - 500) * 100.0) / 100.0)
                .weightGainCalories(Math.round((userTdee + 500) * 100.0) / 100.0)
                .calculationMethod("User Input TDEE")
                .methodDescription("Dựa trên giá trị TDEE bạn đã nhập: " + userTdee + " calories")
                .calculatedAt(LocalDateTime.now())
                .build();
    }
} 