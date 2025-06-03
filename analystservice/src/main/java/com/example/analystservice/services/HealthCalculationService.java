package com.example.analystservice.services;

import com.example.analystservice.dtos.UserDto;
import com.example.analystservice.dtos.HealthMetricsDto;
import com.example.analystservice.dtos.TdeeCalculationDto;
import com.example.analystservice.enums.ActivityLevel;

public interface HealthCalculationService {

    double calculateBmi(double weightKg, double heightCm);

    double calculateBmr(double weightKg, double heightCm, int age, String gender);

    double calculateTdee(double bmr, double activityFactor);

    int calculateAge(java.util.Date birthDate);

    HealthMetricsDto createHealthMetrics(UserDto user);

    TdeeCalculationDto createTdeeCalculation(UserDto user, ActivityLevel activityLevel);

    TdeeCalculationDto createWorkoutBasedTdeeCalculation(UserDto user);

    TdeeCalculationDto createUserInputTdeeCalculation(UserDto user);
}