# Health App Backend - Hệ Thống Microservices

## Tổng Quan Hệ Thống

Hệ thống backend Health App được xây dựng theo kiến trúc microservices với **Load Balancer HAProxy**, bao gồm **6 services chính** phục vụ quản lý sức khỏe và dinh dưỡng.

**🏗️ Kiến Trúc:** Microservices với Service Discovery + Load Balancer  
**🔒 Bảo Mật:** JWT Authentication + Redis Blacklist + SSL/TLS  
**📊 Phân Tích:** Tính toán sức khỏe với MET values  
**⚡ Hiệu Năng:** Auto-calculation engines + High Availability

## 🎯 Tổng Quan Microservices

| Service | Port | Database | Chức Năng Chính |
|---------|------|----------|-----------------|
| **HAProxy Load Balancer** | 80, 443, 8403, 8404 | - | **SSL Termination**, Health Checks, Stats Dashboard |
| **API Gateway (HA)** | 8080, 8081 | - | **Load Balanced** (2 instances), Routing, Auth, Rate Limiting |
| **AuthService** | 8005 | auth_db | JWT Authentication, Token Blacklist, Đăng ký người dùng |
| **UserService** | 8006 | user_db | Quản lý hồ sơ, Activity Levels, Dữ liệu sức khỏe |
| **WorkoutService** | 8007 | workout_db | **Tính toán Calorie theo MET**, Quản lý bài tập |
| **MealService** | 8008 | meal_db | **Đồng bộ Calories tự động**, Theo dõi dinh dưỡng |
| **AnalystService** | 8009 | analyst_db | **Phân tích sức khỏe**, Tính toán BMI/BMR/TDEE |

## 🏗️ Kiến Trúc Hệ Thống

```
                    ┌───────────────────────────┐
                    │        HAProxy            │
                    │    (HTTP: 80, HTTPS: 443) │
                    │    • SSL Termination      │
                    │    • Load Balancing       │
                    │    • Health Checks        │
                    │    • Stats: 8404          │
                    └─────────────┬─────────────┘
                                 │ Round Robin
        ┌────────────────────────┼
        │                        │
┌───────▼────────┐      ┌────────▼────────┐
│  API Gateway 1 │      │  API Gateway 2  │
│  (Port: 8080)  │      │  (Port: 8081)   │
│ • JWT Auth     │      │ • JWT Auth      │
│ • Rate Limit   │      │ • Rate Limit    │
│ • Circuit Break│      │ • Circuit Break │
│ • Routing      │      │ • Routing       │
└───────┬────────┘      └────────┬────────┘
        └────────────────────────┼
                                 │
    ┌────────────┬──────────────┼──────────────┬────────────┬────────────┐
    │            │              │              │            │            │
┌───▼───┐    ┌───▼───┐    ┌─────▼─────┐    ┌───▼───┐    ┌───▼───┐    ┌───▼───┐
│AuthSvc│    │UserSvc│    │WorkoutSvc │    │MealSvc│    │Analyst│    │Eureka │
│ :8005 │    │ :8006 │    │   :8007   │    │ :8008 │    │ :8009 │    │ :8761 │
└───┬───┘    └───┬───┘    └─────┬─────┘    └───┬───┘    └───┬───┘    └───────┘
    │            │              │              │            │
┌───▼───┐    ┌───▼───┐    ┌─────▼─────┐    ┌───▼───┐    ┌───▼───┐
│auth_db│    │user_db│    │workout_db │    │meal_db│    │analyst│
│ MySQL │    │ MySQL │    │   MySQL   │    │ MySQL │    │ MySQL │
└───────┘    └───────┘    └───────────┘    └───────┘    └───────┘
                                │
                          ┌─────▼─────┐
                          │   Redis   │
                          │ (Blacklist)│
                          │   :6379   │
                          └───────────┘
```

## 📊 Chi Tiết Các Services

### 🌐 HAProxy Load Balancer
**Vai trò:** High Availability & SSL Termination

**Chức năng:**
- **SSL/TLS Termination**: Hỗ trợ HTTPS với self-signed certificates
- **Load Balancing**: Phân phối traffic round-robin giữa 2 API Gateway instances
- **Health Checks**: Tự động phát hiện instances bị lỗi qua `/actuator/health`
- **Stats Dashboard**: Giám sát real-time tại **https://localhost:8404**
- **Security Headers**: HSTS, X-Frame-Options, XSS Protection
- **HTTP to HTTPS Redirect**: Tự động chuyển hướng từ HTTP sang HTTPS

### 🌐 API Gateway (High Availability)
**Vai trò:** Điểm vào trung tâm & Security Layer (2 Instances)

**Chức năng:**
- **High Availability**: 2 instances được load balanced bởi HAProxy
- **Định tuyến thông minh**: Routes đến đúng microservice
- **JWT Authentication**: Xác thực tập trung cho tất cả protected endpoints
- **Token Blacklist**: Kiểm tra token bị vô hiệu hóa qua Redis
- **Rate Limiting**: 100 requests/giây per client
- **Circuit Breaker**: Ngăn chặn lỗi lan truyền
- **CORS Support**: Cross-origin resource sharing

**Quy tắc định tuyến:**
- `/auth/**` → AuthService (Public)
- `/users/**` → UserService (Cần xác thực)
- `/workouts/**` → WorkoutService (Cần xác thực)
- `/meals/**` → MealService (Cần xác thực)
- `/analytics/**` → AnalystService (Cần xác thực)

### 🔐 AuthService
**Vai trò:** Xác thực & Phân quyền

**Chức năng:**
- **Đăng ký người dùng**: Tạo tài khoản với validation
- **JWT Authentication**: Đăng nhập bảo mật với token generation
- **Quản lý mật khẩu**: Đổi mật khẩu với token refresh
- **Token Blacklist**: Đăng xuất với Redis-based token revocation
- **Đồng bộ người dùng**: Tự động tạo profile trong UserService

### 👤 UserService
**Vai trò:** Quản lý hồ sơ người dùng & Dữ liệu sức khỏe

**Chức năng:**
- **Quản lý hồ sơ**: Thông tin cá nhân (fullName, email, gender, birthDate)
- **Dữ liệu sức khỏe**: Thông số sức khỏe (weight, height)
- **Activity Levels**: 5 cấp độ từ SEDENTARY (1.2) đến EXTRA_ACTIVE (1.9)
- **Bảo mật**: Bảo vệ email/password không thể cập nhật
- **Tích hợp**: Đồng bộ với AuthService và AnalystService

### 🏃 WorkoutService
**Vai trò:** Quản lý bài tập & Tính toán Calorie theo MET

**Chức năng nâng cao:**
- **Tính toán theo MET**: Tính toán calorie khoa học
  - Công thức: `Calories = MET × cân nặng(kg) × thời gian(giờ)`
- **7 loại bài tập**: CARDIO (7.0), STRENGTH (4.5), HIIT (9.0), CROSSFIT (9.0), YOGA (2.8), PILATES (3.5), FLEXIBILITY (2.5)
- **Quản lý bài tập**: Theo dõi sets, reps, thời gian
- **Quản lý lịch trình**: Lập kế hoạch workout với 5 trạng thái
- **Thống kê**: Phân tích calorie burn hàng ngày/tuần/tháng
- **Tự động tính toán**: JPA lifecycle hooks đảm bảo độ chính xác

### 🍽️ MealService
**Vai trò:** Quản lý dinh dưỡng & Đồng bộ Calories tự động

**Chức năng nâng cao:**
- **Đồng bộ Calories tự động**: `Meal Calories = Tổng(Food Calories)`
- **4 loại bữa ăn**: BREAKFAST, LUNCH, DINNER, SNACK
- **3 trạng thái bữa ăn**: SCHEDULED, COMPLETED, CANCELLED
- **Quản lý thực phẩm**: Theo dõi từng món ăn với calorie tracking
- **Lập kế hoạch**: Lịch trình bữa ăn với khoảng thời gian
- **Thống kê**: Phân tích calorie intake hàng ngày/tuần/tháng
- **Nhất quán dữ liệu**: JPA lifecycle hooks đảm bảo độ chính xác

### 📊 AnalystService
**Vai trò:** Phân tích sức khỏe thông minh

**Chức năng thông minh:**
- **Phân tích sức khỏe**: Tính toán BMI, BMR, TDEE
  - BMR: Phương trình Mifflin-St Jeor
  - TDEE: Tính toán dựa trên hệ số hoạt động
- **Phân tích hoạt động**: Phân tích pattern workout
- **Chiến lược kép**: Tính toán TDEE thủ công vs dựa trên Workout
- **Phân tích Calorie**: So sánh intake vs burn
- **Khuyến nghị thông minh**: Lời khuyên sức khỏe dựa trên BMI categories
- **Tích hợp**: Kết nối UserService và WorkoutService để lấy dữ liệu

## 🔄 Luồng Hoạt Động Hệ Thống

### Luồng xác thực
```
1. Client → API Gateway (/auth/signup hoặc /auth/login)
2. API Gateway → AuthService (xác thực thông tin)
3. AuthService → UserService (tạo/đồng bộ profile)
4. AuthService ← UserService (xác nhận profile)
5. Client ← API Gateway (JWT token + thông tin user)
```

### Luồng business logic
```
1. Client → API Gateway (với JWT token)
2. API Gateway xác thực JWT + kiểm tra Redis blacklist
3. API Gateway → Target Service
4. Target Service xử lý + tự động tính toán
5. Client ← API Gateway (dữ liệu phản hồi)
```

### Giao tiếp giữa các Services
```
- AuthService ↔ UserService (đồng bộ user profile)
- AnalystService → UserService (lấy dữ liệu sức khỏe)
- AnalystService → WorkoutService (lấy dữ liệu hoạt động)
- WorkoutService → UserService (lấy cân nặng cho tính toán MET)
```

## ⚙️ Tech Stack

### Công nghệ cốt lõi
- **Framework**: Spring Boot 3.2.3
- **Ngôn ngữ**: Java 21
- **Bảo mật**: Spring Security + JWT (JJWT 0.11.5)
- **Database**: MySQL 8.0 (mỗi service)
- **Cache**: Redis (token blacklist)
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Resilience**: Resilience4j (Circuit Breaker + Rate Limiter)

### Dependencies
- **ORM**: Spring Data JPA + Hibernate
- **Validation**: Bean Validation (Jakarta Validation)
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **Testing**: Spring Boot Test + Testcontainers
- **Monitoring**: Spring Boot Actuator

## 🚀 Khởi Chạy Nhanh

### Yêu cầu
- **Java 21** (JDK)
- **Maven 3.9+**
- **Docker & Docker Compose**

### Clone Project với Submodules
```bash
# Clone main repository cùng với submodules
git clone --recursive https://github.com/Romlatao/health-app-backend.git

# Hoặc nếu đã clone rồi, cập nhật submodules
git clone https://github.com/Romlatao/health-app-backend.git
cd health-app-backend
git submodule update --init --recursive
```

### Chạy toàn bộ hệ thống
```bash
# Tạo SSL certificates cho HAProxy
cd haproxy && ./generate-ssl.sh && cd ..

# Khởi chạy toàn bộ hệ thống
docker-compose up -d

# Kiểm tra trạng thái các services
docker-compose ps
```

### Điểm truy cập
- **Ứng dụng chính**: https://localhost
- **HAProxy Stats**: https://localhost:8404 (admin/12345)
- **Service Discovery**: http://localhost:8761
- **API Gateway**: http://localhost:8080, http://localhost:8081

## 📚 Tài Liệu Chi Tiết

### Hướng dẫn từng Service
- **[HAProxy Load Balancer](./README_HAPROXY.md)** - Cấu hình load balancer và SSL
- **[API Gateway Service](./apiservice/README.md)** - Gateway, routing và bảo mật
- **[AuthService](./authservice/README.md)** - Xác thực và phân quyền
- **[UserService](./userservice/README.md)** - Quản lý người dùng và hồ sơ
- **[WorkoutService](./workoutservice/README.md)** - Quản lý bài tập và MET calculation
- **[MealService](./mealservice/README.md)** - Quản lý dinh dưỡng và calories
- **[AnalystService](./analystservice/README.md)** - Phân tích sức khỏe và thống kê

### Cấu hình hệ thống
- **Cấu hình HAProxy**: `./haproxy/haproxy.cfg`
- **SSL Setup**: `./haproxy/generate-ssl.sh`
- **Docker Compose**: `./docker-compose.yml`
- **Network**: `health-app-network` (172.16.0.0/16)
