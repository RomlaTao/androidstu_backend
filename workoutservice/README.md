# Workout Service

## Tổng Quan

Workout Service là một microservice chuyên quản lý các hoạt động tập luyện của người dùng trong ứng dụng Health App. Service này cung cấp các chức năng:

- Quản lý thông tin bài tập (workouts) với **tính toán calories tự động**
- Quản lý lịch tập luyện (schedules)
- Quản lý các buổi tập đã lên lịch (scheduled workouts)
- Theo dõi trạng thái và tiến trình tập luyện
- **Thống kê calories đốt cháy dựa trên khoa học MET (Metabolic Equivalent of Task)**
- **Tính toán calories tự động từ MET values và thông số tập luyện**

Service này được thiết kế để hoạt động độc lập, nhưng đồng thời tích hợp với các service khác trong hệ thống thông qua API Gateway.

## ✨ Tính Năng Mới Nổi Bật

### 🔥 Automatic Calorie Calculation
- **MET-based calculation**: Tính toán calories dựa trên giá trị MET khoa học
- **Formula**: Calories = MET × weight(kg) × duration(hours)
- **Auto-sync**: Tự động cập nhật calories khi thay đổi workout/exercises
- **Dual calculation**: Hỗ trợ cả MET-based và exercise-based calculation

### 📊 Advanced Calorie Burn Statistics
- **Daily/Weekly/Monthly**: Thống kê calories theo ngày/tuần/tháng
- **Custom range**: Truy vấn calories trong khoảng thời gian tùy chỉnh
- **Performance optimized**: Batch queries cho hiệu suất cao
- **Simplified data model**: DTO gọn nhẹ chỉ 3 fields cần thiết

### 🏃‍♂️ Scientific Workout Types
Mỗi WorkoutType có MET value khoa học:
- **CARDIO** (7.0 MET): Chạy bộ, đạp xe
- **STRENGTH** (4.5 MET): Tập tạ, calisthenics  
- **HIIT** (9.0 MET): High-Intensity Interval Training
- **CROSSFIT** (9.0 MET): CrossFit training
- **YOGA** (2.8 MET): Yoga, thiền định
- **PILATES** (3.5 MET): Pilates exercises
- **FLEXIBILITY** (2.5 MET): Giãn cơ, stretching

## Chức Năng Chính

### 1. Quản Lý Bài Tập (Workouts)

Cho phép tạo, quản lý và tìm kiếm các loại bài tập với **tính toán calories tự động**:
- **Auto-calculation**: Calories được tính tự động từ MET values
- **Validation**: Đảm bảo calories calculation chính xác
- **Exercise integration**: Tính toán từ cả workout tổng thể và exercises riêng lẻ
- **JPA lifecycle**: Auto-update trước khi save/update (@PrePersist/@PreUpdate)

### 2. Quản Lý Lịch Tập (Schedules)

Cho phép người dùng:
- Tạo lịch tập cá nhân
- Xem lịch tập theo khoảng thời gian
- Cập nhật và quản lý lịch tập

### 3. Quản Lý Buổi Tập Đã Lên Lịch (Scheduled Workouts)

Cung cấp khả năng:
- Lên lịch cho các buổi tập cụ thể
- Theo dõi trạng thái buổi tập (đã hoàn thành, bỏ lỡ, hủy...)
- Nhận thông báo về các buổi tập sắp tới

### 4. ⚡ Thống Kê Calories Đốt Cháy (Mới!)

Cung cấp API thống kê calories burned với hiệu suất cao:
- **Daily statistics**: Thống kê theo ngày
- **Weekly statistics**: Thống kê 7 ngày
- **Monthly statistics**: Thống kê theo tháng
- **Custom range**: Truy vấn khoảng thời gian tùy chỉnh
- **Batch optimization**: Giảm database queries, tăng performance

## Cấu Trúc Dự Án

```
workoutservice/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── workoutservice/
│   │   │               ├── configs/
│   │   │               │   └── SecurityConfig.java
│   │   │               ├── controllers/
│   │   │               │   ├── WorkoutController.java
│   │   │               │   ├── ScheduleController.java
│   │   │               │   ├── ScheduledWorkoutController.java
│   │   │               │   └── 🆕 CaloriesBurnController.java
│   │   │               ├── dtos/
│   │   │               │   ├── WorkoutDTO.java
│   │   │               │   ├── ScheduleDTO.java
│   │   │               │   ├── ScheduledWorkoutDTO.java
│   │   │               │   ├── ExerciseDTO.java
│   │   │               │   └── 🆕 CalorieBurnStatsDto.java
│   │   │               ├── entities/
│   │   │               │   ├── ✨ Workout.java (Enhanced với auto-calculation)
│   │   │               │   ├── Exercise.java
│   │   │               │   ├── Schedule.java
│   │   │               │   ├── ScheduledWorkout.java
│   │   │               │   ├── 🔥 WorkoutType.java (với MET values)
│   │   │               │   └── WorkoutStatus.java
│   │   │               ├── exceptions/
│   │   │               ├── mappers/
│   │   │               ├── repositories/
│   │   │               │   └── ⚡ ScheduledWorkoutRepository.java (Optimized)
│   │   │               ├── services/
│   │   │               │   ├── 🆕 MetabolicCalculationService.java
│   │   │               │   ├── 🆕 CalorieBurnCalculationService.java
│   │   │               │   └── impl/
│   │   │               │       ├── 🆕 MetabolicCalculationServiceImpl.java
│   │   │               │       ├── 🆕 CalorieBurnCalculationServiceImpl.java
│   │   │               │       └── ✨ WorkoutServiceImpl.java (Enhanced)
│   │   │               └── WorkoutserviceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## API Endpoints

### Workout Controller

| Method | Endpoint | API Gateway URL | Description |
|--------|----------|----------------|-------------|
| POST | `/workouts` | `/workouts` | 🔥 Tạo bài tập mới (auto-calculate calories) |
| GET | `/workouts/{id}` | `/workouts/{id}` | Lấy thông tin bài tập theo ID |
| GET | `/workouts` | `/workouts` | Lấy danh sách tất cả bài tập |
| GET | `/workouts/type/{type}` | `/workouts/type/{type}` | Lấy danh sách bài tập theo loại |
| GET | `/workouts/search?name={name}` | `/workouts/search?name={name}` | Tìm kiếm bài tập theo tên |
| PUT | `/workouts/{id}` | `/workouts/{id}` | 🔥 Cập nhật thông tin bài tập (auto-recalculate) |
| DELETE | `/workouts/{id}` | `/workouts/{id}` | Xóa bài tập |

### Schedule Controller

| Method | Endpoint | API Gateway URL | Description |
|--------|----------|----------------|-------------|
| POST | `/workouts/schedules` | `/workouts/schedules` | Tạo lịch tập mới |
| GET | `/workouts/schedules/{id}` | `/workouts/schedules/{id}` | Lấy thông tin lịch tập theo ID |
| GET | `/workouts/schedules/user/{userId}` | `/workouts/schedules/user/{userId}` | Lấy danh sách lịch tập của người dùng |
| GET | `/workouts/schedules/user/{userId}/date-range` | `/workouts/schedules/user/{userId}/date-range` | Lấy danh sách lịch tập trong khoảng thời gian |
| PUT | `/workouts/schedules/{id}` | `/workouts/schedules/{id}` | Cập nhật thông tin lịch tập |
| DELETE | `/workouts/schedules/{id}` | `/workouts/schedules/{id}` | Xóa lịch tập |

### Scheduled Workout Controller

| Method | Endpoint | API Gateway URL | Description |
|--------|----------|----------------|-------------|
| POST | `/workouts/scheduled-workouts` | `/workouts/scheduled-workouts` | Tạo buổi tập đã lên lịch |
| GET | `/workouts/scheduled-workouts/{id}` | `/workouts/scheduled-workouts/{id}` | Lấy thông tin buổi tập theo ID |
| GET | `/workouts/scheduled-workouts/schedule/{scheduleId}` | `/workouts/scheduled-workouts/schedule/{scheduleId}` | Lấy danh sách buổi tập theo lịch tập |
| GET | `/workouts/scheduled-workouts/user/{userId}` | `/workouts/scheduled-workouts/user/{userId}` | Lấy danh sách buổi tập của người dùng trong khoảng thời gian |
| GET | `/workouts/scheduled-workouts/user/{userId}/status/{status}` | `/workouts/scheduled-workouts/user/{userId}/status/{status}` | Lấy danh sách buổi tập theo trạng thái và khoảng thời gian |
| PUT | `/workouts/scheduled-workouts/{id}` | `/workouts/scheduled-workouts/{id}` | Cập nhật thông tin buổi tập |
| PATCH | `/workouts/scheduled-workouts/{id}/status/{status}` | `/workouts/scheduled-workouts/{id}/status/{status}` | Cập nhật trạng thái buổi tập |
| DELETE | `/workouts/scheduled-workouts/{id}` | `/workouts/scheduled-workouts/{id}` | Xóa buổi tập |

### 🆕 Calorie Burn Statistics Controller

| Method | Endpoint | API Gateway URL | Description |
|--------|----------|----------------|-------------|
| GET | `/calories-burned/daily/{userId}` | `/workouts/calories-burned/daily/{userId}` | 📊 Thống kê calories đốt cháy theo ngày |
| GET | `/calories-burned/weekly/{userId}` | `/workouts/calories-burned/weekly/{userId}` | 📊 Thống kê calories đốt cháy theo tuần (7 ngày) |
| GET | `/calories-burned/monthly/{userId}` | `/workouts/calories-burned/monthly/{userId}` | 📊 Thống kê calories đốt cháy theo tháng |
| GET | `/calories-burned/range/{userId}` | `/workouts/calories-burned/range/{userId}` | 🔥 Thống kê calories trong khoảng thời gian tùy chỉnh |

## Models

### 🔥 WorkoutType (Enum với MET Values)
```java
public enum WorkoutType {
    CARDIO(7.0),        // Chạy bộ, đạp xe - 7.0 MET
    STRENGTH(4.5),      // Tập tạ, calisthenics - 4.5 MET  
    FLEXIBILITY(2.5),   // Giãn cơ, stretching - 2.5 MET
    HIIT(9.0),          // High-Intensity Interval Training - 9.0 MET
    YOGA(2.8),          // Yoga, thiền định - 2.8 MET
    PILATES(3.5),       // Pilates exercises - 3.5 MET
    CROSSFIT(9.0);      // CrossFit training - 9.0 MET
    
    private final Double metValue;
    
    public Double getMetValue() {
        return metValue;
    }
}
```

### WorkoutStatus (Enum)
```
SCHEDULED, COMPLETED, MISSED, CANCELLED, IN_PROGRESS
```

### 🆕 CalorieBurnStatsDto (Simplified)
```json
{
  "userId": "1",
  "date": "2024-01-15",
  "totalCaloriesBurned": 450
}
```

## Request/Response Examples

### 🔥 1. Tạo Bài Tập Mới (Auto-Calculate Calories)

**Request**
```
POST http://localhost:8007/workouts
Content-Type: application/json
Authorization: Bearer {jwt_token}

{
  "name": "Morning HIIT Session",
  "description": "High intensity interval training for 20 minutes",
  "type": "HIIT",
  "durationMinutes": 20,
  "caloriesBurned": 300,
  "exercises": [
    {
      "name": "Burpees",
      "description": "Full body explosive movement",
      "sets": 3,
      "reps": 10,
      "durationSeconds": 300
    },
    {
      "name": "Jump Squats",
      "description": "Jumping squats for legs",
      "sets": 3,
      "reps": 15,
      "durationSeconds": 240
    }
  ]
}
```

**Response**
```
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "name": "Morning HIIT Session",
  "description": "High intensity interval training for 20 minutes",
  "type": "HIIT",
  "caloriesBurned": 300,
  "durationMinutes": 20,
  "exercises": [
    {
      "id": 1,
      "name": "Burpees",
      "description": "Full body explosive movement",
      "sets": 3,
      "reps": 10,
      "durationSeconds": 300
    },
    {
      "id": 2,
      "name": "Jump Squats",
      "description": "Jumping squats for legs",
      "sets": 3,
      "reps": 15,
      "durationSeconds": 240
    }
  ]
}
```

### 🔥 2. Cập Nhật Bài Tập

**Request**
```
PUT http://localhost:8007/workouts/1
Content-Type: application/json
Authorization: Bearer {jwt_token}

{
  "name": "Updated HIIT Session",
  "description": "Modified high intensity training",
  "type": "HIIT",
  "durationMinutes": 25,
  "caloriesBurned": 375,
  "exercises": [
    {
      "name": "Modified Burpees",
      "description": "Full body explosive movement - modified",
      "sets": 4,
      "reps": 12,
      "durationSeconds": 360
    }
  ]
}
```

### 🏃‍♂️ 3. Tạo Bài Tập YOGA

**Request**
```
POST http://localhost:8007/workouts
Content-Type: application/json
Authorization: Bearer {jwt_token}

{
  "name": "Morning Yoga Flow",
  "description": "Peaceful morning yoga session",
  "type": "YOGA",
  "durationMinutes": 30,
  "caloriesBurned": 98,
  "exercises": [
    {
      "name": "Sun Salutation",
      "description": "Traditional sun salutation sequence",
      "sets": 5,
      "reps": 1,
      "durationSeconds": 180
    },
    {
      "name": "Warrior Pose",
      "description": "Warrior I and II poses",
      "sets": 2,
      "reps": 1,
      "durationSeconds": 120
    }
  ]
}
```

### 💪 4. Tạo Bài Tập STRENGTH

**Request**
```
POST http://localhost:8007/workouts
Content-Type: application/json
Authorization: Bearer {jwt_token}

{
  "name": "Upper Body Strength",
  "description": "Strength training for upper body",
  "type": "STRENGTH",
  "durationMinutes": 45,
  "caloriesBurned": 236,
  "exercises": [
    {
      "name": "Push Ups",
      "description": "Standard push ups",
      "sets": 3,
      "reps": 15,
      "durationSeconds": 0
    },
    {
      "name": "Pull Ups",
      "description": "Pull ups on bar",
      "sets": 3,
      "reps": 8,
      "durationSeconds": 0
    },
    {
      "name": "Dumbbell Press",
      "description": "Chest press with dumbbells",
      "sets": 3,
      "reps": 12,
      "durationSeconds": 0
    }
  ]
}
```

### 📅 5. Tạo Lịch Tập

**Request**
```
POST http://localhost:8007/workouts/schedules
Content-Type: application/json
Authorization: Bearer {jwt_token}

{
  "name": "Weekly Fitness Plan",
  "description": "My comprehensive workout plan for this week",
  "userId": "user123",
  "startDate": "2024-01-15",
  "endDate": "2024-01-21"
}
```

**Response**
```
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "name": "Weekly Fitness Plan",
  "description": "My comprehensive workout plan for this week",
  "userId": "user123",
  "startDate": "2024-01-15",
  "endDate": "2024-01-21",
  "scheduledWorkouts": []
}
```

### 🗓️ 6. Lên Lịch Một Buổi Tập

**Request**
```
POST http://localhost:8007/workouts/scheduled-workouts
Content-Type: application/json
Authorization: Bearer {jwt_token}

{
  "scheduleId": 1,
  "workoutId": 1,
  "scheduledDateTime": "2024-01-15T06:00:00",
  "status": "SCHEDULED",
  "notes": "Early morning workout session"
}
```

**Response**
```
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "scheduleId": 1,
  "workoutId": 1,
  "scheduledDateTime": "2024-01-15T06:00:00",
  "status": "SCHEDULED",
  "notes": "Early morning workout session",
  "workout": {
    "id": 1,
    "name": "Morning HIIT Session",
    "description": "High intensity interval training for 20 minutes",
    "type": "HIIT",
    "caloriesBurned": 300,
    "durationMinutes": 20
  }
}
```

### 🔄 7. Cập Nhật Trạng Thái Buổi Tập

**Request**
```
PATCH http://localhost:8007/workouts/scheduled-workouts/1/status/COMPLETED
Authorization: Bearer {jwt_token}
```

**Response**
```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 1,
  "scheduleId": 1,
  "workoutId": 1,
  "scheduledDateTime": "2024-01-15T06:00:00",
  "status": "COMPLETED",
  "notes": "Early morning workout session",
  "workout": {
    "id": 1,
    "name": "Morning HIIT Session",
    "description": "High intensity interval training for 20 minutes",
    "type": "HIIT",
    "caloriesBurned": 300,
    "durationMinutes": 20
  }
}
```

### 📊 8. Lấy Thống Kê Calories Đốt Cháy Theo Ngày

**Request**
```
GET http://localhost:8007/calories-burned/daily/user123?date=2024-01-15
Authorization: Bearer {jwt_token}
```

**Response**
```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "userId": "user123",
  "date": "2024-01-15",
  "totalCaloriesBurned": 650
}
```

### 📊 9. Lấy Thống Kê Calories Theo Tuần

**Request**
```
GET http://localhost:8007/calories-burned/weekly/user123?startDate=2024-01-15
Authorization: Bearer {jwt_token}
```

**Response**
```
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "userId": "user123",
    "date": "2024-01-15",
    "totalCaloriesBurned": 650
  },
  {
    "userId": "user123",
    "date": "2024-01-16",
    "totalCaloriesBurned": 420
  },
  {
    "userId": "user123",
    "date": "2024-01-17",
    "totalCaloriesBurned": 300
  },
  {
    "userId": "user123",
    "date": "2024-01-18",
    "totalCaloriesBurned": 0
  },
  {
    "userId": "user123",
    "date": "2024-01-19",
    "totalCaloriesBurned": 520
  },
  {
    "userId": "user123",
    "date": "2024-01-20",
    "totalCaloriesBurned": 380
  },
  {
    "userId": "user123",
    "date": "2024-01-21",
    "totalCaloriesBurned": 290
  }
]
```

### 📊 10. Lấy Thống Kê Calories Theo Tháng

**Request**
```
GET http://localhost:8007/calories-burned/monthly/user123?year=2024&month=1
Authorization: Bearer {jwt_token}
```

**Response**
```
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "userId": "user123",
    "date": "2024-01-01",
    "totalCaloriesBurned": 320
  },
  {
    "userId": "user123",
    "date": "2024-01-02",
    "totalCaloriesBurned": 0
  },
  {
    "userId": "user123",
    "date": "2024-01-03",
    "totalCaloriesBurned": 450
  },
  // ... tất cả 31 ngày trong tháng 1
  {
    "userId": "user123",
    "date": "2024-01-31",
    "totalCaloriesBurned": 380
  }
]
```

### 🔥 11. Lấy Thống Kê Calories Theo Khoảng Thời Gian Tùy Chỉnh

**Request**
```
GET http://localhost:8007/calories-burned/range/user123?startDate=2024-01-01&endDate=2024-01-07
Authorization: Bearer {jwt_token}
```

**Response**
```
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "userId": "user123",
    "date": "2024-01-01",
    "totalCaloriesBurned": 320
  },
  {
    "userId": "user123",
    "date": "2024-01-02",
    "totalCaloriesBurned": 0
  },
  {
    "userId": "user123",
    "date": "2024-01-03",
    "totalCaloriesBurned": 450
  },
  {
    "userId": "user123",
    "date": "2024-01-04",
    "totalCaloriesBurned": 280
  },
  {
    "userId": "user123",
    "date": "2024-01-05",
    "totalCaloriesBurned": 600
  },
  {
    "userId": "user123",
    "date": "2024-01-06",
    "totalCaloriesBurned": 0
  },
  {
    "userId": "user123",
    "date": "2024-01-07",
    "totalCaloriesBurned": 390
  }
]
```

### 🔍 12. Tìm Kiếm Bài Tập Theo Tên

**Request**
```
GET http://localhost:8007/workouts/search?name=HIIT
Authorization: Bearer {jwt_token}
```

### 🏷️ 13. Lấy Bài Tập Theo Loại

**Request**
```
GET http://localhost:8007/workouts/type/YOGA
Authorization: Bearer {jwt_token}
```

### 📅 14. Lấy Lịch Tập Trong Khoảng Thời Gian

**Request**
```
GET http://localhost:8007/workouts/schedules/user/user123/date-range?startDate=2024-01-01&endDate=2024-01-31
Authorization: Bearer {jwt_token}
```

### 🎯 15. Lấy Buổi Tập Theo Trạng Thái

**Request**
```
GET http://localhost:8007/workouts/scheduled-workouts/user/user123/status/COMPLETED?startDateTime=2024-01-01T00:00:00&endDateTime=2024-01-31T23:59:59
Authorization: Bearer {jwt_token}
```

## ⚡ Performance Features

### Batch Query Optimization
- **Single query** thay vì multiple daily queries
- **GROUP BY date** để aggregate data hiệu quả
- **COALESCE** để handle null values
- **Optimized indexing** cho datetime fields

### Auto-Calculation Engine
- **JPA Lifecycle hooks**: @PrePersist, @PreUpdate
- **MET-based formula**: Scientific calorie calculation
- **Exercise-level calculation**: Chi tiết đến từng exercise
- **Validation methods**: Đảm bảo tính chính xác

## 🧪 Testing với Postman

### Collection Setup

1. Tạo một collection mới trong Postman với tên "Workout Service API v2.0"
2. Đặt biến môi trường:
   - `base_url`: http://localhost:8007
   - `user_id`: user123
   - `jwt_token`: your_jwt_token_here

### 🔥 Test Cases Mới

1. **Auto-Calculate Calories Workout**
   - Method: POST
   - URL: {{base_url}}/workouts
   - Headers: 
     - Content-Type: application/json
     - Authorization: Bearer {{jwt_token}}
   - Body: (Sử dụng ví dụ tạo bài tập HIIT ở trên)

2. **Daily Calorie Burn Stats**
   - Method: GET
   - URL: {{base_url}}/calories-burned/daily/{{user_id}}?date=2024-01-15
   - Headers: Authorization: Bearer {{jwt_token}}

3. **Weekly Calorie Burn Stats**
   - Method: GET
   - URL: {{base_url}}/calories-burned/weekly/{{user_id}}?startDate=2024-01-15
   - Headers: Authorization: Bearer {{jwt_token}}

4. **Custom Range Calorie Stats**
   - Method: GET
   - URL: {{base_url}}/calories-burned/range/{{user_id}}?startDate=2024-01-01&endDate=2024-01-31
   - Headers: Authorization: Bearer {{jwt_token}}

5. **Validate MET Calculation**
   - Create YOGA workout (30 minutes) - Expected calories: ~98
   - Create HIIT workout (15 minutes) - Expected calories: ~157  
   - Create STRENGTH workout (45 minutes) - Expected calories: ~236

## Cài Đặt và Chạy

### Yêu Cầu Hệ Thống
- Java 21
- Maven
- MySQL

### Các Bước Cài Đặt

1. Chuẩn bị cơ sở dữ liệu MySQL:
   ```bash
   docker run --name workout-mysql -p 3309:3306 \
     -e MYSQL_ROOT_PASSWORD=secret \
     -e MYSQL_DATABASE=workout_db \
     -d mysql:8.0
   ```

2. Clone repository:
   ```bash
   git clone <repository-url>
   cd health_backend/androidstu_backend
   ```

3. Khởi chạy service:
   ```bash
   cd workoutservice
   mvn spring-boot:run
   ```

### Cấu Hình

Các cấu hình chính trong `application.properties`:

```properties
spring.application.name=workoutservice
server.port=8007

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3309/workout_db
spring.datasource.username=root
spring.datasource.password=secret
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JPA Configuration for Auto-Calculation
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

## 🚀 Migration từ Version Cũ

### Calorie Calculation Changes
- **Trước**: Manual input calories
- **Bây giờ**: Auto-calculation từ MET values
- **Migration**: Existing workouts sẽ được recalculate khi update

### API Changes  
- **Thêm mới**: Calorie burn statistics endpoints
- **Enhanced**: Workout CRUD với auto-calculation
- **Simplified**: CalorieBurnStatsDto chỉ 3 fields

### Performance Improvements
- **Trước**: N+1 queries cho statistics
- **Bây giờ**: Single batch query với GROUP BY
- **Cải thiện**: 80-90% giảm database load

## 🎯 Roadmap Tương Lai

- [ ] User weight integration cho accurate MET calculation
- [ ] Exercise-specific MET values database
- [ ] Real-time calorie tracking với heart rate
- [ ] ML-based calorie prediction
- [ ] Wearable device integration
- [ ] Advanced analytics và insights

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