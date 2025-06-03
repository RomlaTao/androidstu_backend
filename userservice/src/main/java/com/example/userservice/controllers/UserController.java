package com.example.userservice.controllers;

import com.example.userservice.dtos.UserDto;
import com.example.userservice.services.UserService;
import com.example.userservice.entities.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") String id) {
        UserDto userDto = userService.getUserById(id);
        if (userDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với ID: " + id);
        }
        return ResponseEntity.ok(userDto);
    }

    /**
     * Endpoint này chủ yếu được sử dụng bởi AuthService, không phải cho người dùng cuối
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        try {
            return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Đồng bộ user với ID cụ thể từ service khác
     * Endpoint này được sử dụng để đảm bảo userID đồng bộ giữa các database
     */
    @PostMapping("/sync/{id}")
    public ResponseEntity<UserDto> syncUserWithId(
            @PathVariable("id") String id, 
            @Valid @RequestBody UserDto userDto) {
        try {
            return new ResponseEntity<>(userService.syncUserWithId(id, userDto), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    //bỏ
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
        UserDto userDto = userService.getUserByEmail(email);
        if (userDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với email: " + email);
        }
        return ResponseEntity.ok(userDto);
    }

    /**
     * Cập nhật thông tin người dùng
     * Lưu ý: Email và password không thể được cập nhật thông qua endpoint này
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") String id, @Valid @RequestBody UserDto userDto) {
        try {
            UserDto updatedUser = userService.updateUser(id, userDto);
            if (updatedUser == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với ID: " + id);
            }
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Cập nhật Activity Level của người dùng
     * Sử dụng khi chưa thể tính toán AF dựa vào lịch tập trong workoutservice
     */
    @PutMapping("/{id}/activity-level")
    public ResponseEntity<UserDto> updateUserActivityLevel(
            @PathVariable("id") String id, 
            @RequestParam("activityLevel") User.InitialActivityLevel activityLevel) {
        try {
            UserDto updatedUser = userService.updateUserActivityLevel(id, activityLevel);
            if (updatedUser == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với ID: " + id);
            }
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Lấy danh sách Activity Levels có sẵn
     */
    @GetMapping("/activity-levels")
    public ResponseEntity<User.InitialActivityLevel[]> getActivityLevels() {
        return ResponseEntity.ok(User.InitialActivityLevel.values());
    }

    /**
     * Xử lý ngoại lệ chung
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleExceptions(Exception e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
} 