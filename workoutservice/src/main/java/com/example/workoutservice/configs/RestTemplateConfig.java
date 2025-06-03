package com.example.workoutservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.time.Duration;

/**
 * Configuration cho RestTemplate để gọi các microservices khác
 */
@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        
        // Timeout configurations
        factory.setConnectTimeout(Duration.ofSeconds(5)); // Connect timeout
        factory.setConnectionRequestTimeout(Duration.ofSeconds(5)); // Request timeout
        
        RestTemplate restTemplate = new RestTemplate(factory);
        
        // Add error handling interceptors if needed
        return restTemplate;
    }
} 