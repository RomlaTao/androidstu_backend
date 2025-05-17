package com.example.apiservice.configs;

import com.example.apiservice.filters.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    public RouteConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service Routes
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("lb://AUTHSERVICE"))
                
                // User Service Routes (with JWT filter)
                .route("user-service", r -> r
                        .path("/users/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://USERSERVICE"))
                
                // Workout Service Routes (with JWT filter)
                .route("workout-service", r -> r
                        .path("/workouts/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://WORKOUTSERVICE"))
                
                .build();
    }
}