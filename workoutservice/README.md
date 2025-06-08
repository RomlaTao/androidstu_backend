# WorkoutService

## Tổng Quan

WorkoutService là microservice quản lý các hoạt động tập luyện của người dùng trong hệ thống Health App. Service này cung cấp khả năng quản lý bài tập, lịch tập luyện, và tính toán calories đốt cháy tự động dựa trên giá trị MET khoa học.

**Port:** 8007  
**Database:** MySQL workout_db (port 3309)  
**Dependencies:** UserService (8001), Eureka (8761)

## Chức Năng Chính

### 1. Quản Lý Bài Tập (Workouts)
- **Tạo/Sửa/Xóa bài tập**: CRUD operations cho workouts
- **Tính toán calories tự động**: Dựa trên MET values và thông số người dùng
- **7 loại workout**: CARDIO, STRENGTH, HIIT, CROSSFIT, YOGA, PILATES, FLEXIBILITY
- **Quản lý exercises**: Mỗi workout có thể chứa nhiều exercises
- **Tìm kiếm và phân loại**: Theo tên hoặc loại workout

### 2. Quản Lý Lịch Tập (Schedules) 
- **Tạo lịch tập cá nhân**: Lên kế hoạch tập luyện
- **Quản lý thời gian**: Theo khoảng thời gian (startDate - endDate)
- **Theo dõi buổi tập**: Kết nối với scheduled workouts

### 3. Quản Lý Buổi Tập Đã Lên Lịch (Scheduled Workouts)
- **5 trạng thái**: SCHEDULED, COMPLETED, MISSED, CANCELLED, IN_PROGRESS
- **Theo dõi tiến trình**: Cập nhật trạng thái buổi tập
- **Thống kê**: Xem lịch sử và tiến trình tập luyện

### 4. Thống Kê Calories Đốt Cháy
- **Thống kê theo ngày**: Calories burned hàng ngày
- **Thống kê theo tuần**: 7 ngày liên tiếp 
- **Thống kê theo tháng**: Toàn bộ tháng
- **Khoảng thời gian tùy chỉnh**: Flexible date range

## WorkoutType và MET Values

| Loại Workout | MET Value | Mô Tả |
|--------------|----------|-------|
| CARDIO | 7.0 | Chạy bộ, đạp xe |
| STRENGTH | 4.5 | Tập tạ, calisthenics |
| HIIT | 9.0 | High-Intensity Interval Training |
| CROSSFIT | 9.0 | CrossFit training |
| YOGA | 2.8 | Yoga, thiền định |
| PILATES | 3.5 | Pilates exercises |
| FLEXIBILITY | 2.5 | Giãn cơ, stretching |

**Công thức tính calories:** Calories = MET × weight(kg) × duration(hours)

## Cấu Hình

### Cơ Sở Dữ Liệu
```properties
spring.datasource.url=jdbc:mysql://localhost:3309/workout_db
spring.datasource.username=root
spring.datasource.password=secret
```

### Service Discovery
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

### UserService Integration
```properties
userservice.url=http://localhost:8001
```

## API Endpoints

### Workout Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/workouts` | Tạo bài tập mới (auto-calculate calories) |
| GET | `/workouts/{id}` | Lấy bài tập theo ID |
| GET | `/workouts` | Lấy tất cả bài tập |
| GET | `/workouts/type/{type}` | Lấy bài tập theo loại |
| GET | `/workouts/search?name={name}` | Tìm kiếm theo tên |
| PUT | `/workouts/{id}` | Cập nhật bài tập |
| DELETE | `/workouts/{id}` | Xóa bài tập |

### Schedule Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/workouts/schedules` | Tạo lịch tập mới |
| GET | `/workouts/schedules/{id}` | Lấy lịch tập theo ID |
| GET | `/workouts/schedules/user/{userId}` | Lấy lịch tập của user |
| GET | `/workouts/schedules/user/{userId}/date-range` | Lấy lịch theo khoảng thời gian |
| PUT | `/workouts/schedules/{id}` | Cập nhật lịch tập |
| DELETE | `/workouts/schedules/{id}` | Xóa lịch tập |

### Scheduled Workout Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/workouts/scheduled-workouts` | Tạo buổi tập đã lên lịch |
| GET | `/workouts/scheduled-workouts/{id}` | Lấy buổi tập theo ID |
| GET | `/workouts/scheduled-workouts/schedule/{scheduleId}` | Lấy buổi tập theo lịch |
| GET | `/workouts/scheduled-workouts/user/{userId}` | Lấy buổi tập của user |
| GET | `/workouts/scheduled-workouts/user/{userId}/status/{status}` | Lấy theo trạng thái |
| PUT | `/workouts/scheduled-workouts/{id}` | Cập nhật buổi tập |
| PATCH | `/workouts/scheduled-workouts/{id}/status/{status}` | Cập nhật trạng thái |
| DELETE | `/workouts/scheduled-workouts/{id}` | Xóa buổi tập |

### Calorie Statistics
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/workouts/calories-burned/daily/{userId}?date={date}` | Thống kê theo ngày |
| GET | `/workouts/calories-burned/weekly/{userId}?startDate={date}` | Thống kê theo tuần |
| GET | `/workouts/calories-burned/monthly/{userId}?year={year}&month={month}` | Thống kê theo tháng |
| GET | `/workouts/calories-burned/range/{userId}?startDate={start}&endDate={end}` | Thống kê tùy chỉnh |

## Tech Stack

- **Spring Boot:** 3.2.3
- **Java:** 21  
- **Database:** MySQL 8.0
- **ORM:** Spring Data JPA
- **Security:** Spring Security
- **Service Discovery:** Eureka Client
- **Validation:** Bean Validation

## Cài Đặt

### 1. Chuẩn Bị Database
```bash
docker run --name workout-mysql -p 3309:3306 \
  -e MYSQL_ROOT_PASSWORD=secret \
  -e MYSQL_DATABASE=workout_db \
  -d mysql:8.0
```

### 2. Khởi Chạy Service
```bash
cd workoutservice
mvn spring-boot:run
```

## Postman Test Cases

### Environment Variables
```json
{
  "gateway_url": "http://localhost:8080",
  "workout_url": "http://localhost:8007",
  "user_id": "testuser123",
  "access_token": "your_jwt_token"
}
```

### 1. Tạo Bài Tập HIIT
```json
POST {{gateway_url}}/workouts
Authorization: Bearer {{access_token}}
Content-Type: application/json

{
  "name": "Morning HIIT Session",
  "description": "High intensity interval training",
  "type": "HIIT",
  "durationMinutes": 20,
  "exercises": [
    {
      "name": "Burpees",
      "sets": 3,
      "reps": 10,
      "durationSeconds": 300
    }
  ]
}
```

### 2. Tạo Bài Tập YOGA
```json
POST {{gateway_url}}/workouts
Authorization: Bearer {{access_token}}
Content-Type: application/json

{
  "name": "Morning Yoga Flow",
  "description": "Peaceful morning yoga session",
  "type": "YOGA", 
  "durationMinutes": 30,
  "exercises": [
    {
      "name": "Sun Salutation",
      "sets": 5,
      "reps": 1,
      "durationSeconds": 180
    }
  ]
}
```

### 3. Lấy Bài Tập Theo Loại
```json
GET {{gateway_url}}/workouts/type/HIIT
Authorization: Bearer {{access_token}}
```

### 4. Tìm Kiếm Bài Tập
```json
GET {{gateway_url}}/workouts/search?name=yoga
Authorization: Bearer {{access_token}}
```

### 5. Tạo Lịch Tập
```json
POST {{gateway_url}}/workouts/schedules
Authorization: Bearer {{access_token}}
Content-Type: application/json

{
  "name": "Weekly Fitness Plan",
  "description": "Comprehensive workout plan",
  "userId": "{{user_id}}",
  "startDate": "2024-01-15",
  "endDate": "2024-01-21"
}
```

### 6. Lên Lịch Buổi Tập
```json
POST {{gateway_url}}/workouts/scheduled-workouts
Authorization: Bearer {{access_token}}
Content-Type: application/json

{
  "scheduleId": 1,
  "workoutId": 1,
  "scheduledDateTime": "2024-01-15T06:00:00",
  "status": "SCHEDULED",
  "notes": "Early morning session"
}
```

### 7. Cập Nhật Trạng Thái Buổi Tập
```json
PATCH {{gateway_url}}/workouts/scheduled-workouts/1/status/COMPLETED
Authorization: Bearer {{access_token}}
```

### 8. Thống Kê Calories Theo Ngày
```json
GET {{gateway_url}}/workouts/calories-burned/daily/{{user_id}}?date=2024-01-15
Authorization: Bearer {{access_token}}
```

### 9. Thống Kê Calories Theo Tuần
```json
GET {{gateway_url}}/workouts/calories-burned/weekly/{{user_id}}?startDate=2024-01-15
Authorization: Bearer {{access_token}}
```

### 10. Thống Kê Calories Theo Tháng
```json
GET {{gateway_url}}/workouts/calories-burned/monthly/{{user_id}}?year=2024&month=1
Authorization: Bearer {{access_token}}
```

### 11. Thống Kê Calories Tùy Chỉnh
```json
GET {{gateway_url}}/workouts/calories-burned/range/{{user_id}}?startDate=2024-01-01&endDate=2024-01-31
Authorization: Bearer {{access_token}}
```

### 12. Lấy Lịch Theo Khoảng Thời Gian
```json
GET {{gateway_url}}/workouts/schedules/user/{{user_id}}/date-range?startDate=2024-01-01&endDate=2024-01-31
Authorization: Bearer {{access_token}}
```

### 13. Lấy Buổi Tập Theo Trạng Thái
```json
GET {{gateway_url}}/workouts/scheduled-workouts/user/{{user_id}}/status/COMPLETED?startDateTime=2024-01-01T00:00:00&endDateTime=2024-01-31T23:59:59
Authorization: Bearer {{access_token}}
```

### 14. Cập Nhật Bài Tập
```json
PUT {{gateway_url}}/workouts/1
Authorization: Bearer {{access_token}}
Content-Type: application/json

{
  "name": "Updated HIIT Session",
  "description": "Modified training",
  "type": "HIIT",
  "durationMinutes": 25
}
```

### 15. Xóa Bài Tập
```json
DELETE {{gateway_url}}/workouts/1
Authorization: Bearer {{access_token}}
```

## Lưu Ý

- **Auto-calculation**: Calories được tính tự động, không cần manual input
- **MET-based**: Sử dụng giá trị MET khoa học cho độ chính xác cao
- **Performance**: Optimized queries cho thống kê nhanh chóng
- **API Gateway**: Service hoạt động sau lớp API Gateway
- **Authentication**: Xác thực được xử lý ở tầng gateway
- **User ID**: Được truyền từ gateway sau khi xác thực thành công
- **Validation**: Automatic validation cho calorie calculation accuracy 
- **DateTime Format**: Sử dụng ISO-8601 format cho tất cả datetime fields
- **Status Enum**: Các giá trị status phải match chính xác với WorkoutStatus enum
- **Required Fields**: Tất cả fields có annotation @NotNull hoặc @NotBlank là bắt buộc 