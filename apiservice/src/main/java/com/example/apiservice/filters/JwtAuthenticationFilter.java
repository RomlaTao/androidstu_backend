package com.example.apiservice.filters;

import com.example.apiservice.services.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Value("${security.jwt.secret-key}")
    private String secretKey;
    
    private final TokenBlacklistService tokenBlacklistService;
    
    public JwtAuthenticationFilter(TokenBlacklistService tokenBlacklistService) {
        super(Config.class);
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // Skip authentication for auth endpoints
            if (request.getPath().toString().startsWith("/auth/login") || 
                request.getPath().toString().startsWith("/auth/signup")) {
                return chain.filter(exchange);
            }
            
            // Check if request has authorization header
            if (!request.getHeaders().containsKey("Authorization")) {
                return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Invalid Authorization header format", HttpStatus.UNAUTHORIZED);
            }

            // Extract and validate token
            String token = authHeader.substring(7);
            
            // Check if token is blacklisted
            if (tokenBlacklistService.isBlacklisted(token)) {
                return onError(exchange, "Token has been revoked", HttpStatus.UNAUTHORIZED);
            }
            
            try {
                Claims claims = validateToken(token);
                
                // Subject trong JWT chính là email (username) của người dùng
                String userEmail = claims.getSubject();
                
                // Log để debug
                System.out.println("Token subject (email): " + userEmail);
                
                // Add user information to request headers
                ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", claims.getSubject()) // Subject là email trong AuthService
                    .header("X-User-Email", claims.getSubject()) // Subject là email trong AuthService
                    .build();
                
                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            } catch (Exception e) {
                System.out.println("JWT Error: " + e.getClass().getName() + " - " + e.getMessage());
                return onError(exchange, "Invalid token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private Claims validateToken(String token) {
        // Sử dụng phương pháp giống với AuthService
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        // Check if token is expired
        if (claims.getExpiration().before(new Date())) {
            throw new RuntimeException("Token expired");
        }
        
        return claims;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    public static class Config {
        // Configuration properties if needed
    }
}