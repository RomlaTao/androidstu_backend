package com.example.authservice.services;

import com.example.authservice.dtos.RegisterUserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private final String userServiceUrl;

    public UserServiceClient(RestTemplate restTemplate, 
                            @Value("${services.user-service.url}") String userServiceUrl) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }

    public Map<String, Object> createUser(RegisterUserDto registerUserDto) {
        String url = userServiceUrl + "/users";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, Object> userDto = new HashMap<>();
        userDto.put("email", registerUserDto.getEmail());
        userDto.put("fullName", registerUserDto.getFullName());
        
        if (registerUserDto.getGender() != null) {
            userDto.put("gender", registerUserDto.getGender());
        }
        
        if (registerUserDto.getBirthDate() != null) {
            userDto.put("birthDate", registerUserDto.getBirthDate());
        }
        
        if (registerUserDto.getWeight() != null) {
            userDto.put("weight", registerUserDto.getWeight());
        }
        
        if (registerUserDto.getHeight() != null) {
            userDto.put("height", registerUserDto.getHeight());
        }
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(userDto, headers);
        
        return restTemplate.postForObject(url, entity, Map.class);
    }
} 