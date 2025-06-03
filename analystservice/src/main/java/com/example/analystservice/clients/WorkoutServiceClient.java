package com.example.analystservice.clients;

import com.example.analystservice.dtos.CalorieBurnStatsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Component
public class WorkoutServiceClient {

    private final WebClient webClient;

    public WorkoutServiceClient(@Value("${workout.service.url:http://localhost:8007}") String workoutServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(workoutServiceUrl)
                .build();
    }

    /**
     * Lấy thống kê calories đốt cháy theo ngày
     */
    public Mono<CalorieBurnStatsDto> getDailyCaloriesBurned(String userId, LocalDate date) {
        return webClient.get()
                .uri("/calories-burned/daily/{userId}?date={date}", userId, date)
                .retrieve()
                .bodyToMono(CalorieBurnStatsDto.class);
    }

    /**
     * Lấy thống kê calories đốt cháy chi tiết theo ngày
     */
    public Mono<CalorieBurnStatsDto> getDetailedDailyCaloriesBurned(String userId, LocalDate date) {
        return webClient.get()
                .uri("/calories-burned/daily/{userId}/detailed?date={date}", userId, date)
                .retrieve()
                .bodyToMono(CalorieBurnStatsDto.class);
    }

    /**
     * Lấy thống kê calories đốt cháy theo tuần
     */
    public Flux<CalorieBurnStatsDto> getWeeklyCaloriesBurned(String userId, LocalDate startDate) {
        return webClient.get()
                .uri("/calories-burned/weekly/{userId}?startDate={startDate}", userId, startDate)
                .retrieve()
                .bodyToFlux(CalorieBurnStatsDto.class);
    }

    /**
     * Lấy thống kê calories đốt cháy theo tháng
     */
    public Flux<CalorieBurnStatsDto> getMonthlyCaloriesBurned(String userId, int year, int month) {
        return webClient.get()
                .uri("/calories-burned/monthly/{userId}?year={year}&month={month}", userId, year, month)
                .retrieve()
                .bodyToFlux(CalorieBurnStatsDto.class);
    }

    /**
     * Lấy danh sách hoạt động workout của user trong khoảng thời gian
     */
    public List<com.example.analystservice.dtos.WorkoutActivityDto> getUserWorkoutActivities(String userId, java.time.LocalDate startDate, java.time.LocalDate endDate) {
        return webClient.get()
                .uri("/workouts/user/{userId}/activities?startDate={startDate}&endDate={endDate}", userId, startDate, endDate)
                .retrieve()
                .bodyToFlux(com.example.analystservice.dtos.WorkoutActivityDto.class)
                .collectList()
                .block(); // Note: This is blocking, consider making it reactive
    }
}