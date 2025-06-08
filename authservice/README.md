# AuthService

## Chức năng chính

**AuthService** là dịch vụ xác thực trung tâm của hệ thống microservices, chuyên trách quản lý authentication và authorization.

### 🔐 Tính Năng Xác Thực
- **Đăng ký (Signup)**: Tạo tài khoản mới với validation
- **Đăng nhập (Login)**: Xác thực và cấp JWT token
- **Đăng xuất (Logout)**: Vô hiệu hóa token qua blacklist
- **Đổi mật khẩu**: Cập nhật mật khẩu với token mới
- **Quản lý User**: Đồng bộ thông tin với UserService
- **JWT Security**: Token-based authentication
- **Token Blacklist**: Redis-based token revocation

### 🏗️ Kiến Trúc
- **User Entity**: Lưu thông tin xác thực (id, email, password, fullName)
- **JWT Service**: Quản lý token generation/validation
- **Security Config**: Spring Security configuration
- **User Sync**: Tích hợp với UserService để đồng bộ profile

## Cấu hình

### Cấu Hình Cơ Bản
```properties
# Máy chủ
server.port=8005

# Database MySQL
spring.datasource.url=jdbc:mysql://localhost:3307/auth_db
spring.datasource.username=root
spring.datasource.password=secret

# JWT Security
security.jwt.secret-key=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
security.jwt.expiration-time=3600000

# Redis (Token Blacklist)
spring.redis.host=localhost
spring.redis.port=6379

# UserService Integration
services.user-service.url=http://localhost:8006

# Eureka Discovery
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

### Hạ Tầng Cần Thiết
- **MySQL** (port 3307): Database auth_db
- **Redis** (port 6379): Token blacklist storage
- **UserService** (port 8006): User profile management
- **Eureka Server** (port 8761): Service discovery

### Cài Đặt Database
```bash
# MySQL với Docker
docker run --name auth-db -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=auth_db -p 3307:3307 -d mysql:8.0

# Redis với Docker
docker run --name redis-auth -p 6379:6379 -d redis
```

### Ngăn Xếp Công Nghệ
- Spring Boot 3.2.3
- Spring Security + JWT (JJWT 0.11.5)
- MySQL Database + Spring Data JPA
- Redis (Token blacklist)
- Eureka Client

## API Test Cases (Postman)

### Environment Variables
```json
{
  "auth_url": "http://localhost:8080",
  "user_email": "test@example.com",
  "user_password": "password123",
  "new_password": "newpassword456",
  "access_token": ""
}
```

### 🔓 Authentication Tests (Public)

#### 1. Đăng Ký Người Dùng
```json
{
  "method": "POST",
  "url": "{{auth_url}}/auth/signup",
  "headers": {"Content-Type": "application/json"},
  "body": {
    "fullName": "Test User",
    "email": "{{user_email}}",
    "password": "{{user_password}}"
  }
}
```

**Expected Response:**
```json
{
  "message": "Đăng ký tài khoản thành công",
  "userId": "uuid-string",
  "email": "test@example.com",
  "fullName": "Test User"
}
```

#### 2. Đăng Nhập
```json
{
  "method": "POST", 
  "url": "{{auth_url}}/auth/login",
  "headers": {"Content-Type": "application/json"},
  "body": {
    "email": "{{user_email}}",
    "password": "{{user_password}}"
  }
}
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 3600000,
  "userId": "uuid-string",
  "isUserInfoInitialized": false
}
```

### 🔒 Authenticated Tests (Yêu Cầu Token)

#### 3. Đổi Mật Khẩu
```json
{
  "method": "POST",
  "url": "{{auth_url}}/auth/change-password",
  "headers": {
    "Content-Type": "application/json",
    "Authorization": "Bearer {{access_token}}"
  },
  "body": {
    "email": "{{user_email}}",
    "currentPassword": "{{user_password}}",
    "newPassword": "{{new_password}}"
  }
}
```

**Expected Response:**
```json
{
  "message": "Mật khẩu đã được thay đổi thành công",
  "token": "new-jwt-token",
  "expiresIn": 3600000
}
```

#### 4. Đăng Xuất
```json
{
  "method": "POST",
  "url": "{{auth_url}}/auth/logout",
  "headers": {
    "Authorization": "Bearer {{access_token}}"
  }
}
```

**Expected Response:**
```json
{
  "message": "Đăng xuất thành công"
}
```

#### 5. Lấy Thông Tin User Hiện Tại
```json
{
  "method": "GET",
  "url": "{{auth_url}}/auth/users/me",
  "headers": {
    "Authorization": "Bearer {{access_token}}"
  }
}
```

## Luồng Xác Thực
1. **Client** → Đăng ký → **AuthService** → Lưu user → Đồng bộ **UserService**
2. **Client** → Đăng nhập → **AuthService** → Validate → Generate JWT
3. **Client** → Request với JWT → **API Gateway** → Validate JWT
4. **Client** → Đăng xuất → **AuthService** → Blacklist token → **Redis**
5. **UserService** → Complete profile → **AuthService** → Mark initialized 