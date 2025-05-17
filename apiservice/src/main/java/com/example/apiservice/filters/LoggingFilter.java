package com.example.apiservice.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestId = UUID.randomUUID().toString();
        
        logger.info("Request {} started: {} {} from {}",
                requestId,
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                exchange.getRequest().getRemoteAddress());
        
        long startTime = System.currentTimeMillis();
        
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    long endTime = System.currentTimeMillis();
                    logger.info("Request {} completed: {} in {}ms with status {}",
                            requestId,
                            exchange.getRequest().getPath(),
                            endTime - startTime,
                            exchange.getResponse().getStatusCode());
                }));
    }

    @Override
    public int getOrder() {
        return -1; // High priority to ensure this runs first
    }
}