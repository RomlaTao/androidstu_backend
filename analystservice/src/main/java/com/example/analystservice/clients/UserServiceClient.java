package com.example.analystservice.clients;

import com.example.analystservice.dtos.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserServiceClient {

    private final WebClient webClient;
    private final String userServiceUrl;

    public UserServiceClient(WebClient.Builder webClientBuilder,
                             @Value("${services.userservice.url}") String userServiceUrl) {
        this.webClient = webClientBuilder.build();
        this.userServiceUrl = userServiceUrl;
    }

    public Mono<UserDto> getUserById(String userId) {
        return webClient.get()
                .uri(userServiceUrl + "/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserDto.class);
    }

    public Mono<UserDto> getUserByEmail(String email) {
        return webClient.get()
                .uri(userServiceUrl + "/users/email/{email}", email)
                .retrieve()
                .bodyToMono(UserDto.class);
    }
}