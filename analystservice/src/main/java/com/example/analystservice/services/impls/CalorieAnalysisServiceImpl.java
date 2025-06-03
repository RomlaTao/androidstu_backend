package com.example.analystservice.services.impls;

import com.example.analystservice.clients.MealServiceClient;
import com.example.analystservice.clients.UserServiceClient;
import com.example.analystservice.clients.WorkoutServiceClient;
import com.example.analystservice.dtos.CalorieAnalysisDto;
import com.example.analystservice.dtos.CalorieBurnStatsDto;
import com.example.analystservice.dtos.CalorieStatsDto;
import com.example.analystservice.dtos.TdeeCalculationDto;
import com.example.analystservice.dtos.TotalEnergyExpenditureDto;
import com.example.analystservice.dtos.UserDto;
import com.example.analystservice.services.CalorieAnalysisService;
import com.example.analystservice.services.HealthCalculationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalorieAnalysisServiceImpl implements CalorieAnalysisService {
    
    private final MealServiceClient mealServiceClient;
    private final UserServiceClient userServiceClient;
    private final WorkoutServiceClient workoutServiceClient;
    private final HealthCalculationService healthCalculationService;
    
    public CalorieAnalysisServiceImpl(MealServiceClient mealServiceClient,
                                    UserServiceClient userServiceClient,
                                    WorkoutServiceClient workoutServiceClient,
                                    HealthCalculationService healthCalculationService) {
        this.mealServiceClient = mealServiceClient;
        this.userServiceClient = userServiceClient;
        this.workoutServiceClient = workoutServiceClient;
        this.healthCalculationService = healthCalculationService;
    }
    
    @Override
    public Mono<CalorieAnalysisDto> analyzeCaloriesVsTdee(String userId, LocalDate date) {
        // Get user data and calorie stats in parallel
        Mono<UserDto> userMono = userServiceClient.getUserById(userId);
        Mono<CalorieStatsDto> calorieStatsMono = mealServiceClient.getDetailedDailyCalories(userId, date);
        
        return Mono.zip(userMono, calorieStatsMono)
                .flatMap(tuple -> {
                    UserDto user = tuple.getT1();
                    CalorieStatsDto calorieStats = tuple.getT2();
                    
                    return createCalorieAnalysis(user, calorieStats, date);
                });
    }
    
    @Override
    public Mono<TotalEnergyExpenditureDto> calculateTotalEnergyExpenditure(String userId, LocalDate date) {
        Mono<UserDto> userMono = userServiceClient.getUserById(userId);
        Mono<CalorieBurnStatsDto> workoutStatsMono = workoutServiceClient.getDailyCaloriesBurned(userId, date);
        
        return Mono.zip(userMono, workoutStatsMono)
                .map(tuple -> {
                    UserDto user = tuple.getT1();
                    CalorieBurnStatsDto workoutStats = tuple.getT2();
                    
                    // Calculate BMR
                    Double bmr = healthCalculationService.calculateBmr(user.getWeight(), user.getHeight(), user.getAge(), user.getGender());
                    Integer workoutCalories = workoutStats.getTotalCaloriesBurned() != null ? 
                                              workoutStats.getTotalCaloriesBurned() : 0;
                    
                    TotalEnergyExpenditureDto totalExpenditure = new TotalEnergyExpenditureDto(
                        user.getId(), date, bmr, workoutCalories);
                    
                    // Set additional workout stats
                    totalExpenditure.setWorkoutCount(workoutStats.getWorkoutCount());
                    totalExpenditure.setTotalWorkoutDuration(workoutStats.getTotalDurationMinutes());
                    
                    return totalExpenditure;
                });
    }
    
    @Override
    public Mono<TotalEnergyExpenditureDto> getDetailedEnergyExpenditure(String userId, LocalDate date) {
        Mono<UserDto> userMono = userServiceClient.getUserById(userId);
        Mono<CalorieBurnStatsDto> workoutStatsMono = workoutServiceClient.getDetailedDailyCaloriesBurned(userId, date);
        
        return Mono.zip(userMono, workoutStatsMono)
                .map(tuple -> {
                    UserDto user = tuple.getT1();
                    CalorieBurnStatsDto workoutStats = tuple.getT2();
                    
                    // Calculate BMR
                    Double bmr = healthCalculationService.calculateBmr(user.getWeight(), user.getHeight(), user.getAge(), user.getGender());
                    Integer workoutCalories = workoutStats.getTotalCaloriesBurned() != null ? 
                                              workoutStats.getTotalCaloriesBurned() : 0;
                    
                    TotalEnergyExpenditureDto totalExpenditure = new TotalEnergyExpenditureDto(
                        user.getId(), date, bmr, workoutCalories);
                    
                    // Set detailed workout breakdown
                    totalExpenditure.setWorkoutCount(workoutStats.getWorkoutCount());
                    totalExpenditure.setTotalWorkoutDuration(workoutStats.getTotalDurationMinutes());
                    totalExpenditure.setCardioCalories(workoutStats.getCardioCalories());
                    totalExpenditure.setStrengthCalories(workoutStats.getStrengthCalories());
                    totalExpenditure.setYogaCalories(workoutStats.getYogaCalories());
                    totalExpenditure.setOtherCalories(workoutStats.getOtherCalories());
                    
                    // Convert workout details to summary
                    if (workoutStats.getWorkoutDetails() != null) {
                        List<TotalEnergyExpenditureDto.WorkoutSummaryDto> workoutSummary = 
                            workoutStats.getWorkoutDetails().stream()
                                .map(detail -> new TotalEnergyExpenditureDto.WorkoutSummaryDto(
                                    detail.getWorkoutName(),
                                    detail.getWorkoutType(),
                                    detail.getCaloriesBurned(),
                                    detail.getDurationMinutes(),
                                    detail.getScheduledTime()
                                ))
                                .collect(Collectors.toList());
                        totalExpenditure.setWorkoutSummary(workoutSummary);
                    }
                    
                    return totalExpenditure;
                });
    }
    
    @Override
    public Mono<List<TotalEnergyExpenditureDto>> calculateWeeklyEnergyExpenditure(String userId, LocalDate startDate) {
        Mono<UserDto> userMono = userServiceClient.getUserById(userId);
        
        return userMono.flatMap(user -> {
            // Get workout data for the week
            return workoutServiceClient.getWeeklyCaloriesBurned(userId, startDate)
                .collectList()
                .map(weeklyWorkoutStats -> {
                    Double bmr = healthCalculationService.calculateBmr(user.getWeight(), user.getHeight(), user.getAge(), user.getGender());
                    
                    return weeklyWorkoutStats.stream()
                        .map(workoutStats -> {
                            Integer workoutCalories = workoutStats.getTotalCaloriesBurned() != null ? 
                                                      workoutStats.getTotalCaloriesBurned() : 0;
                            
                            TotalEnergyExpenditureDto dailyExpenditure = new TotalEnergyExpenditureDto(
                                user.getId(), workoutStats.getDate(), bmr, workoutCalories);
                            
                            dailyExpenditure.setWorkoutCount(workoutStats.getWorkoutCount());
                            dailyExpenditure.setTotalWorkoutDuration(workoutStats.getTotalDurationMinutes());
                            
                            return dailyExpenditure;
                        })
                        .collect(Collectors.toList());
                });
        });
    }
    
    @Override
    public Mono<List<TotalEnergyExpenditureDto>> calculateMonthlyEnergyExpenditure(String userId, int year, int month) {
        Mono<UserDto> userMono = userServiceClient.getUserById(userId);
        
        return userMono.flatMap(user -> {
            // Get workout data for the month
            return workoutServiceClient.getMonthlyCaloriesBurned(userId, year, month)
                .collectList()
                .map(monthlyWorkoutStats -> {
                    Double bmr = healthCalculationService.calculateBmr(user.getWeight(), user.getHeight(), user.getAge(), user.getGender());
                    
                    return monthlyWorkoutStats.stream()
                        .map(workoutStats -> {
                            Integer workoutCalories = workoutStats.getTotalCaloriesBurned() != null ? 
                                                      workoutStats.getTotalCaloriesBurned() : 0;
                            
                            TotalEnergyExpenditureDto dailyExpenditure = new TotalEnergyExpenditureDto(
                                user.getId(), workoutStats.getDate(), bmr, workoutCalories);
                            
                            dailyExpenditure.setWorkoutCount(workoutStats.getWorkoutCount());
                            dailyExpenditure.setTotalWorkoutDuration(workoutStats.getTotalDurationMinutes());
                            
                            return dailyExpenditure;
                        })
                        .collect(Collectors.toList());
                });
        });
    }
    
    private Mono<CalorieAnalysisDto> createCalorieAnalysis(UserDto user, CalorieStatsDto calorieStats, LocalDate date) {
        return Mono.fromCallable(() -> {
            CalorieAnalysisDto analysis = new CalorieAnalysisDto();
            
            // Basic info
            analysis.setUserId(user.getId());
            analysis.setFullName(user.getFullName());
            analysis.setAnalysisDate(date);
            
            // Calorie intake data
            analysis.setCaloriesConsumed(calorieStats.getTotalCalories());
            analysis.setBreakfastCalories(calorieStats.getBreakfastCalories());
            analysis.setLunchCalories(calorieStats.getLunchCalories());
            analysis.setDinnerCalories(calorieStats.getDinnerCalories());
            analysis.setSnackCalories(calorieStats.getSnackCalories());
            analysis.setMealCount(calorieStats.getMealCount());
            
            // Calculate TDEE - Priority: User Input > Workout-based > Calculated
            TdeeCalculationDto tdeeCalc = determineBestTdee(user);
            analysis.setUserTdee(tdeeCalc.getTdee());
            analysis.setTdeeSource(tdeeCalc.getCalculationMethod());
            analysis.setBmr(tdeeCalc.getBmr());
            analysis.setActivityFactor(tdeeCalc.getActivityFactor());
            
            // Analysis calculations
            int caloriesConsumed = calorieStats.getTotalCalories() != null ? calorieStats.getTotalCalories() : 0;
            double tdee = tdeeCalc.getTdee();
            
            int calorieDeficit = (int) (tdee - caloriesConsumed);
            analysis.setCalorieDeficit(calorieDeficit);
            
            // Weight change estimation (1 pound = 3500 calories)
            double weeklyWeightChange = -(calorieDeficit * 7) / 7700.0; // kg per week
            analysis.setWeightChangeEstimate(Math.round(weeklyWeightChange * 100.0) / 100.0);
            
            // Goal status
            if (Math.abs(calorieDeficit) <= 150) {
                analysis.setWeightGoalStatus("maintenance");
            } else if (calorieDeficit > 0) {
                analysis.setWeightGoalStatus("deficit");
            } else {
                analysis.setWeightGoalStatus("surplus");
            }
            
            // Accuracy percentage
            double accuracy = Math.max(0, 100 - (Math.abs(calorieDeficit) / tdee * 100));
            analysis.setCalorieAccuracy(Math.round(accuracy * 100.0) / 100.0);
            
            // Generate insights and advice
            analysis.setInsights(generateInsights(calorieStats, calorieDeficit, tdee));
            analysis.setNutritionAdvice(generateNutritionAdvice(calorieStats, calorieDeficit));
            analysis.setOverallAssessment(generateOverallAssessment(calorieDeficit, caloriesConsumed, tdee));
            
            return analysis;
        });
    }
    
    private TdeeCalculationDto determineBestTdee(UserDto user) {
        // Priority: User Input > Workout-based > Calculated
        try {
            if (user.getUserInputTdee() != null) {
                return healthCalculationService.createUserInputTdeeCalculation(user);
            }
        } catch (Exception e) {
            // Fall through to workout-based
        }
        
        try {
            return healthCalculationService.createWorkoutBasedTdeeCalculation(user);
        } catch (Exception e) {
            // Fall through to calculated
        }
        
        // Default to calculated TDEE
        return healthCalculationService.createTdeeCalculation(user, 
            com.example.analystservice.enums.ActivityLevel.MODERATELY_ACTIVE);
    }
    
    private List<String> generateInsights(CalorieStatsDto stats, int calorieDeficit, double tdee) {
        List<String> insights = new ArrayList<>();
        
        if (Math.abs(calorieDeficit) <= 150) {
            insights.add("�� Calories nạp vào gần đúng với TDEE - tốt cho duy trì cân nặng");
        } else if (calorieDeficit > 500) {
            insights.add("⚠️ Deficit calories cao (" + calorieDeficit + ") - có thể ảnh hưởng đến năng lượng");
        } else if (calorieDeficit < -500) {
            insights.add("📈 Surplus calories cao (" + Math.abs(calorieDeficit) + ") - có thể tăng cân nhanh");
        }
        
        // Meal distribution analysis
        int totalCalories = stats.getTotalCalories() != null ? stats.getTotalCalories() : 0;
        if (totalCalories > 0) {
            double breakfastPercent = (stats.getBreakfastCalories() != null ? stats.getBreakfastCalories() : 0) * 100.0 / totalCalories;
            double dinnerPercent = (stats.getDinnerCalories() != null ? stats.getDinnerCalories() : 0) * 100.0 / totalCalories;
            
            if (breakfastPercent < 20) {
                insights.add("🌅 Bữa sáng quá ít calories (" + Math.round(breakfastPercent) + "%) - nên tăng cường");
            }
            if (dinnerPercent > 40) {
                insights.add("🌙 Bữa tối quá nhiều calories (" + Math.round(dinnerPercent) + "%) - nên giảm");
            }
        }
        
        if (stats.getMealCount() != null && stats.getMealCount() < 3) {
            insights.add("🍽️ Số bữa ăn ít (" + stats.getMealCount() + ") - nên chia nhỏ thành nhiều bữa");
        }
        
        return insights;
    }
    
    private String generateNutritionAdvice(CalorieStatsDto stats, int calorieDeficit) {
        if (calorieDeficit > 500) {
            return "Nên tăng calories từ protein và carb phức hợp để duy trì năng lượng. " +
                   "Thêm các bữa snack lành mạnh giữa các bữa chính.";
        } else if (calorieDeficit < -300) {
            return "Nên giảm calories từ đồ ngọt và thức ăn nhanh. " +
                   "Tăng cường rau xanh và protein nạc để tạo cảm giác no lâu hơn.";
        } else {
            return "Duy trì chế độ ăn hiện tại, chú ý cân bằng dinh dưỡng. " +
                   "Đảm bảo đủ protein, vitamin và khoáng chất.";
        }
    }
    
    private String generateOverallAssessment(int calorieDeficit, int caloriesConsumed, double tdee) {
        double accuracyPercent = Math.max(0, 100 - (Math.abs(calorieDeficit) / tdee * 100));
        
        if (accuracyPercent >= 90) {
            return "Tuyệt vời! Calories nạp vào rất phù hợp với mục tiêu.";
        } else if (accuracyPercent >= 75) {
            return "Tốt! Calories nạp vào khá phù hợp, có thể điều chỉnh nhẹ.";
        } else if (accuracyPercent >= 60) {
            return "Cần điều chỉnh calories để đạt mục tiêu tốt hơn.";
        } else {
            return "Cần điều chỉnh đáng kể calories để phù hợp với mục tiêu.";
        }
    }
} 