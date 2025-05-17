# Workout Service

## Tổng Quan

Workout Service là một microservice chuyên quản lý các hoạt động tập luyện của người dùng trong ứng dụng Health App. Service này cung cấp các chức năng:

- Quản lý thông tin bài tập (workouts)
- Quản lý lịch tập luyện (schedules)
- Quản lý các buổi tập đã lên lịch (scheduled workouts)
- Theo dõi trạng thái và tiến trình tập luyện

Service này được thiết kế để hoạt động độc lập, nhưng đồng thời tích hợp với các service khác trong hệ thống thông qua API Gateway.

## Chức Năng Chính

### 1. Quản Lý Bài Tập (Workouts)

Cho phép tạo, quản lý và tìm kiếm các loại bài tập khác nhau:
- Cardio (chạy bộ, đạp xe...)
- Strength (tập tạ, calisthenics...)
- Flexibility (yoga, giãn cơ...)
- HIIT (High-Intensity Interval Training)
- Yoga, Pilates, CrossFit
- Bài tập tùy chỉnh (Custom)

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
│   │   │               │   └── ScheduledWorkoutController.java
│   │   │               ├── dtos/
│   │   │               │   ├── WorkoutDTO.java
│   │   │               │   ├── ScheduleDTO.java
│   │   │               │   └── ScheduledWorkoutDTO.java
│   │   │               ├── entities/
│   │   │               │   ├── Workout.java
│   │   │               │   ├── Exercise.java
│   │   │               │   ├── Schedule.java
│   │   │               │   ├── ScheduledWorkout.java
│   │   │               │   ├── WorkoutType.java
│   │   │               │   └── WorkoutStatus.java
│   │   │               ├── exceptions/
│   │   │               ├── mappers/
│   │   │               ├── repositories/
│   │   │               ├── services/
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
| POST | `/api/workouts` | `/workouts` | Tạo bài tập mới |
| GET | `/api/workouts/{id}` | `/workouts/{id}` | Lấy thông tin bài tập theo ID |
| GET | `/api/workouts` | `/workouts` | Lấy danh sách tất cả bài tập |
| GET | `/api/workouts/type/{type}` | `/workouts/type/{type}` | Lấy danh sách bài tập theo loại |
| GET | `/api/workouts/search?name={name}` | `/workouts/search?name={name}` | Tìm kiếm bài tập theo tên |
| PUT | `/api/workouts/{id}` | `/workouts/{id}` | Cập nhật thông tin bài tập |
| DELETE | `/api/workouts/{id}` | `/workouts/{id}` | Xóa bài tập |

### Schedule Controller

| Method | Endpoint | API Gateway URL | Description |
|--------|----------|----------------|-------------|
| POST | `/api/schedules` | `/workouts/schedules` | Tạo lịch tập mới |
| GET | `/api/schedules/{id}` | `/workouts/schedules/{id}` | Lấy thông tin lịch tập theo ID |
| GET | `/api/schedules/user/{userId}` | `/workouts/schedules/user/{userId}` | Lấy danh sách lịch tập của người dùng |
| GET | `/api/schedules/user/{userId}/date-range` | `/workouts/schedules/user/{userId}/date-range` | Lấy danh sách lịch tập trong khoảng thời gian |
| PUT | `/api/schedules/{id}` | `/workouts/schedules/{id}` | Cập nhật thông tin lịch tập |
| DELETE | `/api/schedules/{id}` | `/workouts/schedules/{id}` | Xóa lịch tập |

### Scheduled Workout Controller

| Method | Endpoint | API Gateway URL | Description |
|--------|----------|----------------|-------------|
| POST | `/api/scheduled-workouts` | `/workouts/scheduled-workouts` | Tạo buổi tập đã lên lịch |
| GET | `/api/scheduled-workouts/{id}` | `/workouts/scheduled-workouts/{id}` | Lấy thông tin buổi tập theo ID |
| GET | `/api/scheduled-workouts/schedule/{scheduleId}` | `/workouts/scheduled-workouts/schedule/{scheduleId}` | Lấy danh sách buổi tập theo lịch tập |
| GET | `/api/scheduled-workouts/user/{userId}` | `/workouts/scheduled-workouts/user/{userId}` | Lấy danh sách buổi tập của người dùng trong khoảng thời gian |
| GET | `/api/scheduled-workouts/user/{userId}/status/{status}` | `/workouts/scheduled-workouts/user/{userId}/status/{status}` | Lấy danh sách buổi tập theo trạng thái và khoảng thời gian |
| PUT | `/api/scheduled-workouts/{id}` | `/workouts/scheduled-workouts/{id}` | Cập nhật thông tin buổi tập |
| PATCH | `/api/scheduled-workouts/{id}/status/{status}` | `/workouts/scheduled-workouts/{id}/status/{status}` | Cập nhật trạng thái buổi tập |
| DELETE | `/api/scheduled-workouts/{id}` | `/workouts/scheduled-workouts/{id}` | Xóa buổi tập |

## Models

### WorkoutType (Enum)
```
CARDIO, STRENGTH, FLEXIBILITY, HIIT, YOGA, PILATES, CROSSFIT, CUSTOM
```

### WorkoutStatus (Enum)
```
SCHEDULED, COMPLETED, MISSED, CANCELLED, IN_PROGRESS
```

## Request/Response Examples

### Tạo Bài Tập Mới

**Request**
```
POST /workouts
Content-Type: application/json
Authorization: Bearer {jwt_token}

{
  "name": "Morning Run",
  "description": "30 minute run at moderate pace",
  "type": "CARDIO",
  "caloriesBurned": 300,
  "durationMinutes": 30,
  "difficulty": "MEDIUM"
}
```

**Response**
```
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "name": "Morning Run",
  "description": "30 minute run at moderate pace",
  "type": "CARDIO",
  "caloriesBurned": 300,
  "durationMinutes": 30,
  "difficulty": "MEDIUM",
  "createdAt": "2023-10-28T10:00:00",
  "updatedAt": "2023-10-28T10:00:00"
}
```

### Tạo Lịch Tập

**Request**
```
POST /workouts/schedules
Content-Type: application/json
Authorization: Bearer {jwt_token}

{
  "name": "Weekly Fitness Plan",
  "description": "My workout plan for this week",
  "userId": 1,
  "startDate": "2023-10-30",
  "endDate": "2023-11-05"
}
```

**Response**
```
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "name": "Weekly Fitness Plan",
  "description": "My workout plan for this week",
  "userId": 1,
  "startDate": "2023-10-30",
  "endDate": "2023-11-05",
  "createdAt": "2023-10-28T10:00:00",
  "updatedAt": "2023-10-28T10:00:00"
}
```

### Lên Lịch Một Buổi Tập

**Request**
```
POST /workouts/scheduled-workouts
Content-Type: application/json
Authorization: Bearer {jwt_token}

{
  "scheduleId": 1,
  "workoutId": 1,
  "startDateTime": "2023-10-30T06:00:00",
  "endDateTime": "2023-10-30T06:30:00",
  "status": "SCHEDULED"
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
  "workoutName": "Morning Run",
  "startDateTime": "2023-10-30T06:00:00",
  "endDateTime": "2023-10-30T06:30:00",
  "status": "SCHEDULED",
  "notes": null,
  "createdAt": "2023-10-28T10:00:00",
  "updatedAt": "2023-10-28T10:00:00"
}
```

## Cài Đặt và Chạy

### Yêu Cầu Hệ Thống
- Java 21
- Maven
- MySQL

### Các Bước Cài Đặt

1. Chuẩn bị cơ sở dữ liệu MySQL:
   ```
   docker run --name workout-mysql -p 3309:3306 -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=workout_db -d mysql:8.0
   ```

2. Clone repository:
   ```
   git clone <repository-url>
   cd health_backend/androidstu_backend
   ```

3. Khởi chạy service:
   ```
   cd workoutservice
   mvn spring-boot:run
   ```

### Cấu Hình

Các cấu hình chính trong `application.properties`:

```
spring.application.name=workoutservice
server.port=8007

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3309/workout_db
spring.datasource.username=root
spring.datasource.password=secret
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

## Thử Nghiệm với Postman

### Collection Setup

1. Tạo một collection mới trong Postman với tên "Workout Service API"
2. Đặt biến môi trường:
   - `base_url`: http://localhost:8080

### Test Cases

1. **Xem Tất Cả Bài Tập**
   - Method: GET
   - URL: {{base_url}}/workouts
   - Headers: Authorization: Bearer {jwt_token}
   - Expected: 200 OK với danh sách bài tập

2. **Tạo Bài Tập Mới**
   - Method: POST
   - URL: {{base_url}}/workouts
   - Headers: 
     - Content-Type: application/json
     - Authorization: Bearer {jwt_token}
   - Body:
     ```json
     {
       "name": "Push-ups",
       "description": "Basic upper body exercise",
       "type": "STRENGTH",
       "caloriesBurned": 100,
       "durationMinutes": 10,
       "difficulty": "MEDIUM"
     }
     ```
   - Expected: 201 Created với thông tin bài tập mới

3. **Tạo Lịch Tập**
   - Method: POST
   - URL: {{base_url}}/workouts/schedules
   - Headers: 
     - Content-Type: application/json
     - Authorization: Bearer {jwt_token}
   - Body:
     ```json
     {
       "name": "Daily Strength Plan",
       "description": "Focus on building muscle",
       "userId": 1,
       "startDate": "2023-11-01",
       "endDate": "2023-11-07"
     }
     ```
   - Expected: 201 Created với thông tin lịch tập

4. **Lên Lịch Buổi Tập**
   - Method: POST
   - URL: {{base_url}}/workouts/scheduled-workouts
   - Headers: 
     - Content-Type: application/json
     - Authorization: Bearer {jwt_token}
   - Body:
     ```json
     {
       "scheduleId": 1,
       "workoutId": 1,
       "startDateTime": "2023-11-01T07:00:00",
       "endDateTime": "2023-11-01T07:30:00",
       "status": "SCHEDULED"
     }
     ```
   - Expected: 201 Created với thông tin buổi tập đã lên lịch

5. **Đánh Dấu Buổi Tập Đã Hoàn Thành**
   - Method: PATCH
   - URL: {{base_url}}/workouts/scheduled-workouts/1/status/COMPLETED
   - Headers: Authorization: Bearer {jwt_token}
   - Expected: 200 OK với trạng thái đã cập nhật

## Lưu Ý

- Service này hoạt động sau lớp API Gateway, nên thông thường client sẽ không truy cập trực tiếp mà sẽ thông qua gateway
- Xác thực và phân quyền được xử lý ở tầng API Gateway, nên các endpoint trong Workout Service không yêu cầu xác thực JWT
- Các ID người dùng (userId) được truyền từ gateway sau khi xác thực thành công 