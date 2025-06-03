package com.example.analystservice.services;

import com.example.analystservice.dtos.ActivityAnalysisDto;

public interface ActivityFactorCalculationService {
    ActivityAnalysisDto calculateActivityFactorFromWorkouts(String userId);
}