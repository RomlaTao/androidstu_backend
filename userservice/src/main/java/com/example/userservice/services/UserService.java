package com.example.userservice.services;

import com.example.userservice.clients.AuthServiceClient;
import com.example.userservice.dtos.UserDto;
import com.example.userservice.entities.User;
import com.example.userservice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthServiceClient authServiceClient;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthServiceClient authServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authServiceClient = authServiceClient;
    }

    /**
     * Lấy danh sách tất cả users
     * @return Danh sách UserDto
     */
    public List<UserDto> getAllUsers() {
        logger.debug("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin user theo ID
     * @param id ID của user cần tìm
     * @return UserDto nếu tìm thấy
     */
    public UserDto getUserById(String id) {
        logger.debug("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    /**
     * Lấy thông tin user theo email
     * @param email Email của user cần tìm
     * @return UserDto nếu tìm thấy
     */
    public UserDto getUserByEmail(String email) {
        logger.debug("Fetching user with email: {}", email);
        return userRepository.findByEmail(email)
                .map(this::convertToDto)
                .orElse(null);
    }

    /**
     * Tạo mới user khi được gọi từ AuthService
     * @param userDto Thông tin user cần tạo
     * @return UserDto của user đã tạo
     */
    @Transactional
    public UserDto createUser(UserDto userDto) {
        logger.debug("Creating new user with email: {}", userDto.getEmail());
        
        // Kiểm tra xem user đã tồn tại trong UserService chưa
        if (userRepository.existsByEmail(userDto.getEmail())) {
            logger.info("User with email {} already exists, updating instead", userDto.getEmail());
            // Nếu đã tồn tại thì lấy ra và cập nhật
            User existingUser = userRepository.findByEmail(userDto.getEmail()).get();
            
            if (userDto.getFullName() != null) {
                existingUser.setFullName(userDto.getFullName());
            }
            
            existingUser.setGender(userDto.getGender());
            existingUser.setBirthDate(userDto.getBirthDate());
            existingUser.setWeight(userDto.getWeight());
            existingUser.setHeight(userDto.getHeight());
            
            User updatedUser = userRepository.save(existingUser);
            return convertToDto(updatedUser);
        }
        
        // Tạo mới nếu chưa tồn tại
        User user;
        if (userDto.getId() != null) {
            // Nếu AuthService đã tạo ID, sử dụng ID đó
            user = new User(userDto.getId());
            BeanUtils.copyProperties(userDto, user, "id");
        } else {
            // Nếu không có ID, tạo UUID mới
            user = new User();
            BeanUtils.copyProperties(userDto, user, "id");
        }
        
        // Khi được gọi từ AuthService, password sẽ là null
        // Vì password đã được xử lý và lưu trong AuthService
        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else {
            // Đặt một giá trị để tránh lỗi null, vì đây chỉ là thông tin người dùng
            // còn authentication được xử lý ở AuthService
            user.setPassword("NOT_USED_FOR_AUTH");
        }
        
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with id: {}", savedUser.getId());
        
        return convertToDto(savedUser);
    }

    /**
     * Đồng bộ user với ID cụ thể từ service khác
     * @param id ID cụ thể cần sử dụng
     * @param userDto Thông tin user
     * @return UserDto của user đã tạo
     */
    @Transactional
    public UserDto syncUserWithId(String id, UserDto userDto) {
        logger.debug("Syncing user with specific id: {}", id);
        
        // Kiểm tra xem user với ID này đã tồn tại chưa
        if (userRepository.existsById(id)) {
            logger.info("User with id {} already exists, updating instead", id);
            return updateUser(id, userDto);
        }
        
        // Tạo user với ID cụ thể
        User user = new User(id);
        BeanUtils.copyProperties(userDto, user, "id");
        
        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else {
            user.setPassword("NOT_USED_FOR_AUTH");
        }
        
        User savedUser = userRepository.save(user);
        logger.info("User synced successfully with id: {}", savedUser.getId());
        
        return convertToDto(savedUser);
    }

    /**
     * Cập nhật thông tin user
     * @param id ID của user cần cập nhật
     * @param userDto Thông tin mới
     * @return UserDto của user sau khi cập nhật
     * @throws IllegalArgumentException nếu cố gắng cập nhật email hoặc password
     */
    @Transactional
    public UserDto updateUser(String id, UserDto userDto) {
        logger.debug("Updating user with id: {}", id);

        // Không cho phép cập nhật email từ UserService
        if (userDto.getEmail() != null) {
            logger.warn("Attempt to update email is not allowed in UserService");
            throw new IllegalArgumentException("Email không thể được cập nhật từ UserService. Vui lòng liên hệ với quản trị viên.");
        }

        // Không cho phép cập nhật password từ UserService
        if (userDto.getPassword() != null) {
            logger.warn("Attempt to update password is not allowed in UserService");
            throw new IllegalArgumentException("Password không thể được cập nhật từ UserService. Vui lòng sử dụng AuthService để thay đổi mật khẩu.");
        }
        
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            logger.warn("User with id {} not found", id);
            return null;
        }
        
        // Chỉ cập nhật các thông tin cá nhân và sức khỏe
        if (userDto.getFullName() != null) {
            user.setFullName(userDto.getFullName());
        }
        
        if (userDto.getGender() != null) {
            user.setGender(userDto.getGender());
        }
        
        if (userDto.getBirthDate() != null) {
            user.setBirthDate(userDto.getBirthDate());
        }
        
        if (userDto.getWeight() != null) {
            user.setWeight(userDto.getWeight());
        }
        
        if (userDto.getHeight() != null) {
            user.setHeight(userDto.getHeight());
        }
        
        if (userDto.getInitialActivityLevel() != null) {
            user.setInitialActivityLevel(userDto.getInitialActivityLevel());
        }
        
        User updatedUser = userRepository.save(user);
        logger.info("User updated successfully with id: {}", updatedUser.getId());
        
        // Gọi AuthService để đánh dấu user đã hoàn thành khởi tạo thông tin
        // Điều này được gọi khi user cập nhật thông tin cá nhân lần đầu
        try {
            authServiceClient.markUserInfoInitialized(id);
        } catch (Exception e) {
            logger.warn("Failed to mark user info as initialized for user {}: {}", id, e.getMessage());
            // Không throw exception để không ảnh hưởng đến việc cập nhật user
        }
        
        return convertToDto(updatedUser);
    }

    /**
     * Cập nhật Activity Level của user
     * @param id ID của user cần cập nhật
     * @param activityLevel Activity Level mới
     * @return UserDto của user sau khi cập nhật
     */
    @Transactional
    public UserDto updateUserActivityLevel(String id, User.InitialActivityLevel activityLevel) {
        logger.debug("Updating user activity level with id: {} to level: {}", id, activityLevel);
        
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            logger.warn("User with id {} not found", id);
            return null;
        }
        
        user.setInitialActivityLevel(activityLevel);
        
        User updatedUser = userRepository.save(user);
        logger.info("User activity level updated successfully with id: {} to level: {}", 
                   updatedUser.getId(), activityLevel);
        
        return convertToDto(updatedUser);
    }

    /**
     * Xóa user theo ID
     * @param id ID của user cần xóa
     */
    @Transactional
    public void deleteUser(String id) {
        logger.debug("Deleting user with id: {}", id);
        userRepository.deleteById(id);
        logger.info("User deleted successfully with id: {}", id);
    }
    
    // Phương thức chuyển đổi Entity sang DTO
    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        // Không bao gồm password trong response
        userDto.setPassword(null);
        return userDto;
    }
    
    // Phương thức chuyển đổi DTO sang Entity
    private User convertToEntity(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return user;
    }
} 