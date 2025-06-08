# UserService

## Chức năng chính

**UserService** là dịch vụ quản lý hồ sơ người dùng trong hệ thống microservices, chuyên trách lưu trữ và quản lý thông tin cá nhân của người dùng.

### 👤 Quản Lý Hồ Sơ Người Dùng
- **User Profile**: Quản lý thông tin cá nhân (fullName, email, gender, birthDate)
- **Health Info**: Quản lý thông tin sức khỏe (weight, height)
- **Activity Level**: Quản lý mức độ hoạt động ban đầu (5 levels từ Sedentary đến Extra Active)
- **User Sync**: Đồng bộ dữ liệu với AuthService
- **Profile Validation**: Validation đầy đủ cho dữ liệu đầu vào
- **Security**: Bảo vệ email/password không thể cập nhật

### 🔗 Tích Hợp Hệ Thống
- **AuthService Integration**: Đồng bộ user sau registration
- **JWT Authentication**: Xác thực qua shared JWT secret
- **Profile Completion**: Thông báo AuthService khi hoàn tất profile
- **Separation of Concerns**: Tách biệt auth và profile management

### 📊 Activity Level Management
- **5 Activity Levels**: SEDENTARY (1.2) → EXTRA_ACTIVE (1.9)
- **Factor Calculation**: Mỗi level có factor cho TDEE calculation
- **Timestamp Tracking**: Theo dõi thời gian set activity level
- **Integration Ready**: Chuẩn bị cho AnalystService sử dụng

## Cấu hình

### Cấu Hình Cơ Bản
```properties
# Máy chủ
server.port=8006

# Database MySQL
spring.datasource.url=jdbc:mysql://localhost:3308/user_db
spring.datasource.username=root
spring.datasource.password=secret

# JWT Security (Shared với AuthService)
security.jwt.secret-key=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
security.jwt.expiration-time=3600000

# AuthService Integration
services.authservice.url=http://localhost:8005

# Swagger Documentation
springdoc.swagger-ui.path=/swagger-ui.html

# Eureka Discovery
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

### Hạ Tầng Cần Thiết
- **MySQL** (port 3308): Database user_db
- **AuthService** (port 8005): Authentication service
- **Eureka Server** (port 8761): Service discovery
- **API Gateway** (port 8080): Request routing

### Cài Đặt Database
```bash
# MySQL với Docker
docker run --name user-db -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=user_db -p 3308:3308 -d mysql:8.0
```

### Ngăn Xếp Công Nghệ
- Spring Boot 3.2.3
- Spring Security + JWT (JJWT 0.11.5)
- MySQL Database + Spring Data JPA
- Lombok (Code generation)
- SpringDoc OpenAPI
- Eureka Client

## API Test Cases (Postman)

### Environment Variables
```json
{
  "gateway_url": "http://localhost:8080",
  "user_email": "test@example.com",
  "user_password": "password123",
  "access_token": "",
  "user_id": ""
}
```

### 🔐 Prerequisite: Get Authentication Token

#### 1. Login (via AuthService)
```json
{
  "method": "POST",
  "url": "{{gateway_url}}/auth/login",
  "headers": {"Content-Type": "application/json"},
  "body": {
    "email": "{{user_email}}",
    "password": "{{user_password}}"
  }
}
```

### 👤 User Profile Tests (Auth Required)

#### 2. Lấy Thông Tin User Theo ID
```json
{
  "method": "GET",
  "url": "{{gateway_url}}/users/{{user_id}}",
  "headers": {
    "Authorization": "Bearer {{access_token}}"
  }
}
```

#### 3. Lấy Danh Sách Tất Cả Users
```json
{
  "method": "GET",
  "url": "{{gateway_url}}/users",
  "headers": {
    "Authorization": "Bearer {{access_token}}"
  }
}
```

#### 4. Cập Nhật Thông Tin User
```json
{
  "method": "PUT",
  "url": "{{gateway_url}}/users/{{user_id}}",
  "headers": {
    "Content-Type": "application/json",
    "Authorization": "Bearer {{access_token}}"
  },
  "body": {
    "fullName": "Updated User Name",
    "gender": "MALE",
    "birthDate": "1990-01-15",
    "weight": 75.5,
    "height": 175.0
  }
}
```

**Expected Response:**
```json
{
  "id": "user-id",
  "fullName": "Updated User Name",
  "email": "test@example.com",
  "gender": "MALE",
  "birthDate": "1990-01-15",
  "weight": 75.5,
  "height": 175.0,
  "initialActivityLevel": null,
  "createdAt": "2023-12-07T10:00:00",
  "updatedAt": "2023-12-07T10:30:00"
}
```

### 📊 Activity Level Tests

#### 5. Lấy Danh Sách Activity Levels
```json
{
  "method": "GET",
  "url": "{{gateway_url}}/users/activity-levels",
  "headers": {
    "Authorization": "Bearer {{access_token}}"
  }
}
```

**Expected Response:**
```json
[
  "SEDENTARY",
  "LIGHTLY_ACTIVE", 
  "MODERATELY_ACTIVE",
  "VERY_ACTIVE",
  "EXTRA_ACTIVE"
]
```

#### 6. Cập Nhật Activity Level
```json
{
  "method": "PUT",
  "url": "{{gateway_url}}/users/{{user_id}}/activity-level?activityLevel=MODERATELY_ACTIVE",
  "headers": {
    "Authorization": "Bearer {{access_token}}"
  }
}
```

### 🔧 Sync Operations (Internal)

#### 7. Đồng Bộ User Với ID Cụ Thể
```json
{
  "method": "POST",
  "url": "{{gateway_url}}/users/sync/{{user_id}}",
  "headers": {"Content-Type": "application/json"},
  "body": {
    "fullName": "Synced User",
    "email": "sync@example.com",
    "gender": "FEMALE",
    "birthDate": "1995-05-20",
    "weight": 60.0,
    "height": 165.0
  }
}
```

## Activity Level Reference
| Level | Factor | Mô tả |
|-------|--------|-------|
| SEDENTARY | 1.2 | Ít hoặc không tập thể dục |
| LIGHTLY_ACTIVE | 1.375 | Tập nhẹ 1-3 ngày/tuần |
| MODERATELY_ACTIVE | 1.55 | Tập vừa 3-5 ngày/tuần |
| VERY_ACTIVE | 1.725 | Tập nặng 6-7 ngày/tuần |
| EXTRA_ACTIVE | 1.9 | Tập rất nặng + công việc thể lực |

## Luồng Hoạt Động
1. **AuthService** → Signup → **UserService** (sync user với cùng ID)
2. **Client** → Update profile → **UserService** → **AuthService** (mark completed)
3. **Client** → Set activity level → **UserService** (lưu với timestamp)
4. **AnalystService** → Lấy user info → **UserService** (cho health calculations) 