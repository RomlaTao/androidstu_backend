package com.example.analystservice.services;

import com.example.analystservice.dtos.CalorieAnalysisDto;
import com.example.analystservice.dtos.TotalEnergyExpenditureDto;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface CalorieAnalysisService {
    
    /**
     * Phân tích calories nạp vào so với TDEE để đưa ra insights
     */
    Mono<CalorieAnalysisDto> analyzeCaloriesVsTdee(String userId, LocalDate date);
    
    /**
     * Tính tổng năng lượng tiêu thụ hàng ngày (BMR + calories từ workout)
     */
    Mono<TotalEnergyExpenditureDto> calculateTotalEnergyExpenditure(String userId, LocalDate date);
    
    /**
     * Tính tổng năng lượng tiêu thụ theo tuần
     */
    Mono<List<TotalEnergyExpenditureDto>> calculateWeeklyEnergyExpenditure(String userId, LocalDate startDate);
    
    /**
     * Tính tổng năng lượng tiêu thụ theo tháng
     */
    Mono<List<TotalEnergyExpenditureDto>> calculateMonthlyEnergyExpenditure(String userId, int year, int month);
    
    /**
     * Lấy thống kê chi tiết tổng năng lượng tiêu thụ (bao gồm breakdown workout)
     */
    Mono<TotalEnergyExpenditureDto> getDetailedEnergyExpenditure(String userId, LocalDate date);
} 