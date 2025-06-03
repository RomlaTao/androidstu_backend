package com.example.authservice.controllers;

import com.example.authservice.dtos.ChangePasswordDto;
import com.example.authservice.entities.User;
import com.example.authservice.dtos.LoginUserDto;
import com.example.authservice.dtos.RegisterUserDto;
import com.example.authservice.responses.LoginResponse;
import com.example.authservice.services.AuthenticationService;
import com.example.authservice.services.JwtService;
import com.example.authservice.services.TokenBlacklistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, TokenBlacklistService tokenBlacklistService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Đăng ký tài khoản thành công");
        response.put("userId", registeredUser.getId());
        response.put("email", registeredUser.getEmail());
        response.put("fullName", registeredUser.getFullName());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse()
                .setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime())
                .setUserId(authenticatedUser.getId())
                .setIsUserInfoInitialized(authenticatedUser.getIsUserInfoInitialized());
        return ResponseEntity.ok(loginResponse);
    }
    
    /**
     * Thay đổi mật khẩu người dùng
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        try {
            User user = authenticationService.changePassword(changePasswordDto);
            
            // Tạo token mới sau khi đổi mật khẩu
            String jwtToken = jwtService.generateToken(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Mật khẩu đã được thay đổi thành công");
            response.put("token", jwtToken);
            response.put("expiresIn", jwtService.getExpirationTime());
            
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Lấy token từ header
            String jwt = authHeader.substring(7);
            
            try {
                // Tính toán thời gian còn lại của token
                long expiration = jwtService.getExpirationFromToken(jwt);
                long currentTime = System.currentTimeMillis();
                long ttl = Math.max(0, expiration - currentTime);
                
                if (ttl > 0) {
                    // Thêm token vào blacklist
                    tokenBlacklistService.blacklistToken(jwt, ttl);
                    return ResponseEntity.ok(Map.of("message", "Đăng xuất thành công"));
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Token không hợp lệ"));
            }
        }
        
        return ResponseEntity.badRequest().body(Map.of("error", "Không tìm thấy token"));
    }

    /**
     * Internal endpoint for UserService to update user info initialization status
     * Called when user completes profile update in UserService
     */
    @PutMapping("/internal/mark-user-info-initialized/{userId}")
    public ResponseEntity<?> markUserInfoInitialized(@PathVariable String userId) {
        try {
            User user = authenticationService.completeUserInfo(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getId());
            response.put("isUserInfoInitialized", user.getIsUserInfoInitialized());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi: " + e.getMessage()));
        }
    }
}