package com.example.analystservice.clients;

import com.example.analystservice.dtos.CalorieStatsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
public class MealServiceClient {

    private final WebClient webClient;

    public MealServiceClient(@Value("${meal.service.url:http://localhost:8084}") String mealServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(mealServiceUrl)
                .build();
    }

    /**
     * Lấy thống kê calories theo ngày
     */
    public Mono<CalorieStatsDto> getDailyCalories(String userId, LocalDate date) {
        return webClient.get()
                .uri("/calories/daily/{userId}?date={date}", userId, date)
                .retrieve()
                .bodyToMono(CalorieStatsDto.class);
    }

    /**
     * Lấy thống kê calories chi tiết theo ngày
     */
    public Mono<CalorieStatsDto> getDetailedDailyCalories(String userId, LocalDate date) {
        return webClient.get()
                .uri("/calories/daily/{userId}/detailed?date={date}", userId, date)
                .retrieve()
                .bodyToMono(CalorieStatsDto.class);
    }

    /**
     * Lấy thống kê calories theo tuần
     */
    public Flux<CalorieStatsDto> getWeeklyCalories(String userId, LocalDate startDate) {
        return webClient.get()
                .uri("/calories/weekly/{userId}?startDate={startDate}", userId, startDate)
                .retrieve()
                .bodyToFlux(CalorieStatsDto.class);
    }

    /**
     * Lấy thống kê calories theo tháng
     */
    public Flux<CalorieStatsDto> getMonthlyCalories(String userId, int year, int month) {
        return webClient.get()
                .uri("/calories/monthly/{userId}?year={year}&month={month}", userId, year, month)
                .retrieve()
                .bodyToFlux(CalorieStatsDto.class);
    }

    /**
     * Lấy thống kê calories trong khoảng thời gian
     */
    public Flux<CalorieStatsDto> getCaloriesInRange(String userId, LocalDate startDate, LocalDate endDate) {
        return webClient.get()
                .uri("/calories/range/{userId}?startDate={startDate}&endDate={endDate}", userId, startDate, endDate)
                .retrieve()
                .bodyToFlux(CalorieStatsDto.class);
    }
} 