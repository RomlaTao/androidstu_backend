# User Info Initialization Feature

## Mô tả
Tính năng `isUserInfoInitialized` được thêm vào AuthService để theo dõi xem user đã hoàn thành việc khởi tạo thông tin cá nhân hay chưa. Điều này giúp phân biệt giữa lần đăng nhập đầu tiên và các lần đăng nhập tiếp theo.

## Cách hoạt động

### 1. Khi user đăng ký mới
- Cờ `isUserInfoInitialized` được đặt mặc định là `false`
- User cần hoàn thành việc điền thông tin cá nhân (height, weight, birth date, v.v.)

### 2. Khi user đăng nhập
- API login sẽ trả về thông tin `isUserInfoInitialized` trong response
- Client có thể sử dụng thông tin này để điều hướng user:
  - Nếu `false`: Điều hướng đến trang setup thông tin cá nhân
  - Nếu `true`: Điều hướng đến trang chính của app

### 3. Khi user hoàn thành setup
- User PUT thông tin cá nhân lên UserService
- UserService tự động gọi AuthService để đánh dấu `isUserInfoInitialized = true`
- Các lần đăng nhập sau sẽ trả về `isUserInfoInitialized = true`

## API Endpoints

### 1. Login (AuthService)
```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password"
}
```

**Response:**
```json
{
  "token": "jwt-token",
  "expiresIn": 3600000,
  "userId": "user-uuid",
  "isUserInfoInitialized": false
}
```

### 2. Update User Info (UserService)
```http
PUT /users/{userId}
Content-Type: application/json

{
  "fullName": "John Doe",
  "height": 175.5,
  "weight": 70.0,
  "birthDate": "1990-01-15T00:00:00.000Z",
  "gender": "MALE",
  "initialActivityLevel": "MODERATELY_ACTIVE"
}
```

**Kết quả:**
- UserService cập nhật thông tin user
- UserService tự động gọi AuthService để đánh dấu `isUserInfoInitialized = true`
- Lần đăng nhập tiếp theo sẽ trả về `isUserInfoInitialized = true`

### 3. Internal Endpoint (AuthService)
```http
PUT /auth/internal/mark-user-info-initialized/{userId}
```
*Endpoint này chỉ được UserService gọi, không dành cho client.*

## Database Migration

Chạy script migration để thêm column mới:

```sql
-- Add isUserInfoInitialized column to users table
ALTER TABLE users 
ADD COLUMN is_user_info_initialized BOOLEAN NOT NULL DEFAULT FALSE;

-- Set existing users to have isUserInfoInitialized = true 
UPDATE users SET is_user_info_initialized = TRUE WHERE id IS NOT NULL;
```

## Workflow Ứng dụng

```
1. User đăng ký → isUserInfoInitialized = false
2. User đăng nhập → Client nhận isUserInfoInitialized = false
3. Client điều hướng user đến trang Setup Profile
4. User điền đầy đủ thông tin cá nhân
5. Client gọi PUT /users/{userId} với thông tin cá nhân
6. UserService cập nhật thông tin và gọi AuthService
7. AuthService đánh dấu isUserInfoInitialized = true
8. User đăng nhập lần sau → isUserInfoInitialized = true
9. Client điều hướng user đến trang chính
```

## Configuration

### AuthService
- Thêm endpoint `/auth/internal/mark-user-info-initialized/{userId}`
- Cập nhật User entity với field `isUserInfoInitialized`
- Cập nhật LoginResponse để trả về cờ này

### UserService
- Thêm AuthServiceClient để gọi AuthService
- Cập nhật method `updateUser()` để gọi AuthService khi user cập nhật thông tin
- Thêm configuration `services.authservice.url=http://localhost:8080`

## Lưu ý

- Cờ này được tự động cập nhật khi user PUT thông tin lần đầu tiên
- Không cần client gọi endpoint riêng để đánh dấu hoàn thành
- Đối với user hiện tại: Migration script sẽ đặt `isUserInfoInitialized = true`
- UserService sẽ gọi AuthService một cách bất đồng bộ, không ảnh hưởng đến performance 