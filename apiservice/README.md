# API Gateway Service (apiservice)

## Tổng Quan

API Gateway là điểm vào chính của hệ thống microservices của ứng dụng Health App. Service này đóng vai trò là một lớp trung gian giữa client và các microservices bên trong, cung cấp các tính năng:

- **Định tuyến (Routing)**: Chuyển hướng yêu cầu đến đúng service dựa trên URL pattern
- **Xác thực tập trung (Centralized Authentication)**: Kiểm tra và xác thực JWT token cho tất cả các request
- **Kiểm tra token bị vô hiệu hóa (Token Blacklist)**: Sử dụng Redis để kiểm tra các token đã bị đăng xuất/vô hiệu hóa
- **Circuit Breaker**: Ngăn chặn cascade failure khi một service bị lỗi
- **Rate Limiting**: Giới hạn số lượng request từ client trong một khoảng thời gian
- **Logging**: Ghi lại thông tin về các request và response

## Cấu Trúc Dự Án

```
apiservice/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── apiservice/
│   │   │               ├── configs/
│   │   │               │   ├── CircuitBreakerConfiguration.java
│   │   │               │   ├── CorsConfig.java
│   │   │               │   ├── RateLimiterConfiguration.java
│   │   │               │   ├── RedisConfig.java
│   │   │               │   └── RouteConfig.java
│   │   │               ├── filters/
│   │   │               │   ├── JwtAuthenticationFilter.java
│   │   │               │   └── LoggingFilter.java
│   │   │               ├── services/
│   │   │               │   └── TokenBlacklistService.java
│   │   │               └── ApiserviceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Chức Năng Chính

### 1. Định Tuyến (RouteConfig.java)

Cấu hình định tuyến các request từ client đến các microservices bên trong dựa trên URL pattern:
- `/auth/**` → AuthService
- `/users/**` → UserService
- `/workouts/**` → WorkoutService

Các route đến UserService và WorkoutService đều được bảo vệ bởi JWT Authentication Filter.

### 2. Xác Thực JWT (JwtAuthenticationFilter.java)

Filter xác thực mọi request bằng cách:
- Kiểm tra token có tồn tại trong header Authorization không
- Xác nhận token có cấu trúc đúng và chưa hết hạn
- Kiểm tra token có trong blacklist không (thông qua TokenBlacklistService)
- Trích xuất thông tin người dùng từ token và đính kèm vào request headers

### 3. Quản Lý Token Blacklist (TokenBlacklistService.java)

Dịch vụ này sử dụng Redis để lưu trữ và kiểm tra các token đã bị vô hiệu hóa:
- `isBlacklisted(String token)`: Kiểm tra xem token có trong blacklist hay không

### 4. Circuit Breaker (CircuitBreakerConfiguration.java)

Sử dụng Resilience4j để triển khai mẫu Circuit Breaker, ngăn chặn ảnh hưởng từ việc fail của một service đến toàn bộ hệ thống.
- Ngắt kết nối tạm thời đến các service bị lỗi
- Trả về phản hồi graceful thay vì cho phép request timeout

### 5. Rate Limiting (RateLimiterConfiguration.java)

Giới hạn số lượng request từ client trong một khoảng thời gian nhất định:
- Cấu hình mặc định: 100 request mỗi giây
- Phòng tránh DOS attack và quá tải hệ thống

## Hướng Dẫn Cài Đặt

### Yêu Cầu Hệ Thống

- Java 21
- Maven
- Docker (cho Redis và các microservices khác)
- Eureka Server đang chạy (Service Discovery)

### Các Bước Cài Đặt

1. Clone repository
   ```
   git clone <repository-url>
   cd health_backend/androidstu_backend
   ```

2. Cài đặt Redis bằng Docker
   ```
   docker run --name redis -p 6379:6379 -d redis
   ```

3. Khởi chạy Eureka Server (nếu chưa chạy)

4. Khởi chạy API Gateway
   ```
   cd apiservice
   mvn spring-boot:run
   ```

## Test Cases với Postman (mẫu)

### 1. Đăng Ký Người Dùng

- **Endpoint**: `POST http://localhost:8080/auth/signup`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "fullName": "Test User"
  }
  ```
- **Expected Response**: 201 Created với thông tin người dùng đã đăng ký

### 2. Đăng Nhập

- **Endpoint**: `POST http://localhost:8080/auth/login`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Expected Response**: 200 OK với JWT token
  ```json
  {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600
  }
  ```

### 3. Lấy Thông Tin Người Dùng (Yêu Cầu Xác Thực)

- **Endpoint**: `GET http://localhost:8080/auth/users/me`
- **Headers**: `Authorization: Bearer <accessToken từ bước đăng nhập>`
- **Expected Response**: 200 OK với thông tin người dùng

### 4. Lấy Danh Sách Bài Tập (Yêu Cầu Xác Thực)

- **Endpoint**: `GET http://localhost:8080/workouts`
- **Headers**: `Authorization: Bearer <accessToken từ bước đăng nhập>`
- **Expected Response**: 200 OK với danh sách bài tập

### 5. Tạo Bài Tập Mới (Yêu Cầu Xác Thực)

- **Endpoint**: `POST http://localhost:8080/workouts`
- **Headers**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <accessToken từ bước đăng nhập>`
- **Body**:
  ```json
  {
    "name": "Morning Run",
    "description": "30 minute run at moderate pace",
    "type": "CARDIO",
    "durationMinutes": 30,
    "caloriesBurned": 300
  }
  ```
- **Expected Response**: 201 Created với thông tin bài tập mới

### 6. Tạo Lịch Tập (Yêu Cầu Xác Thực)

- **Endpoint**: `POST http://localhost:8080/workouts/schedules`
- **Headers**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <accessToken từ bước đăng nhập>`
- **Body**:
  ```json
  {
    "name": "Weekly Fitness Plan",
    "description": "My workout plan for this week",
    "userId": 1,
    "startDate": "2023-10-30",
    "endDate": "2023-11-05"
  }
  ```
- **Expected Response**: 201 Created với thông tin lịch tập

### 7. Test Token Blacklist (Đăng Xuất)

- **Endpoint**: `POST http://localhost:8080/auth/logout`
- **Headers**: `Authorization: Bearer <accessToken từ bước đăng nhập>`
- **Expected Response**: 200 OK

- **Test Verification**: Thử gọi bất kỳ API yêu cầu xác thực nào với token đã đăng xuất
- **Expected Response**: 401 Unauthorized với thông báo "Token has been revoked"

### 8. Test Rate Limiting

- Gửi nhiều hơn 100 request trong 1 giây đến bất kỳ endpoint nào
- **Expected Response** sau 100 request: 429 Too Many Requests

## Lưu Ý

- Đảm bảo tất cả các microservices (AuthService, UserService, WorkoutService) đã được đăng ký với Eureka Server
- Mật khẩu trong chuỗi kết nối Redis có thể cần được thay đổi tùy theo cấu hình của bạn
- Đối với môi trường sản xuất, hãy thay đổi `security.jwt.secret-key` thành một giá trị an toàn và duy nhất 