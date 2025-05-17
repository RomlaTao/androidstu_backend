package com.example.apiservice.errors;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Order(-2) // High precedence to override default handlers
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatusCode statusCode;
        String message;

        if (ex instanceof NotFoundException) {
            statusCode = HttpStatus.SERVICE_UNAVAILABLE;
            message = "Service is currently unavailable";
        } else if (ex instanceof ResponseStatusException) {
            statusCode = ((ResponseStatusException) ex).getStatusCode();
            message = ex.getMessage();
        } else {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Internal Server Error";
        }

        exchange.getResponse().setStatusCode(statusCode);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        HttpStatus status = statusCode instanceof HttpStatus ? 
                (HttpStatus) statusCode : HttpStatus.valueOf(statusCode.value());

        String errorJson = String.format("{\"status\":%d,\"error\":\"%s\",\"message\":\"%s\"}",
                status.value(), status.getReasonPhrase(), message);

        DataBuffer buffer = exchange.getResponse().bufferFactory()
                .wrap(errorJson.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}