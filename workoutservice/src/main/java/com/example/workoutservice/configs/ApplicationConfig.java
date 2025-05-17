package com.example.workoutservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@Configuration
public class ApplicationConfig {

    @Bean
    public HandlerExceptionResolver customHandlerExceptionResolver() {
        return new ExceptionHandlerExceptionResolver();
    }
} 