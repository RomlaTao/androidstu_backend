package com.example.authservice.services;

import com.example.authservice.dtos.ChangePasswordDto;
import com.example.authservice.dtos.LoginUserDto;
import com.example.authservice.dtos.RegisterUserDto;
import com.example.authservice.entities.User;
import com.example.authservice.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserServiceClient userServiceClient;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            UserServiceClient userServiceClient
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userServiceClient = userServiceClient;
    }

    @Transactional
    public User signup(RegisterUserDto input) {
        // 1. Lưu thông tin xác thực trong auth service
        User user = new User()
                .setFullName(input.getFullName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()));

        User savedUser = userRepository.save(user);
        
        // 2. Đồng bộ user với ID cụ thể sang UserService để đảm bảo ID giống nhau
        try {
            userServiceClient.syncUserWithId(savedUser.getId(), input);
        } catch (Exception e) {
            // Xử lý lỗi khi gọi UserService
            // Trong trường hợp lỗi, có thể rollback giao dịch hoặc xử lý theo cách khác
            throw new RuntimeException("Failed to sync user to UserService: " + e.getMessage(), e);
        }

        return savedUser;
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
    
    /**
     * Thay đổi mật khẩu người dùng
     * @param changePasswordDto DTO chứa thông tin thay đổi mật khẩu
     * @return User sau khi đã cập nhật mật khẩu
     * @throws BadCredentialsException nếu mật khẩu hiện tại không đúng
     * @throws IllegalArgumentException nếu không tìm thấy người dùng
     */
    @Transactional
    public User changePassword(ChangePasswordDto changePasswordDto) {
        // Xác thực với mật khẩu hiện tại
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            changePasswordDto.getEmail(),
                            changePasswordDto.getCurrentPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Mật khẩu hiện tại không đúng");
        }
        
        // Tìm người dùng
        User user = userRepository.findByEmail(changePasswordDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với email: " + changePasswordDto.getEmail()));
        
        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        
        // Lưu vào database
        return userRepository.save(user);
    }

    /**
     * Đánh dấu user đã hoàn thành khởi tạo thông tin cá nhân
     * @param userId ID của user cần cập nhật
     * @return User sau khi đã cập nhật
     * @throws IllegalArgumentException nếu không tìm thấy người dùng
     */
    @Transactional
    public User completeUserInfo(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId));
        
        // Đánh dấu đã hoàn thành khởi tạo thông tin
        user.setIsUserInfoInitialized(true);
        
        // Lưu vào database
        return userRepository.save(user);
    }

    /**
     * Lấy thông tin user theo ID
     * @param userId ID của user cần lấy thông tin
     * @return User
     * @throws IllegalArgumentException nếu không tìm thấy người dùng
     */
    @Transactional(readOnly = true)
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId));
    }
}