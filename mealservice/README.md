# MealService

## Tổng Quan

MealService là microservice quản lý bữa ăn, thực phẩm và lịch ăn uống của người dùng trong hệ thống Health App. Service này cung cấp khả năng quản lý bữa ăn với **tính toán calories tự động** từ tổng calories của các thực phẩm thành phần.

**Port:** 8008  
**Database:** MySQL meal_db (port 3310)  
**Dependencies:** Eureka (8761)

## Chức Năng Chính

### 1. Quản Lý Bữa Ăn (Meals)
- **CRUD operations**: Tạo/Sửa/Xóa bữa ăn
- **Auto-calculate calories**: Calories = tổng calories của foods
- **4 loại bữa ăn**: BREAKFAST, LUNCH, DINNER, SNACK
- **Quản lý thực phẩm**: Mỗi meal có nhiều foods
- **Tìm kiếm**: Theo tên và loại bữa ăn

### 2. Quản Lý Lịch Ăn Uống (Schedules)
- **Tạo lịch ăn cá nhân**: Kế hoạch ăn uống theo thời gian
- **Quản lý thời gian**: Theo khoảng thời gian (startDate - endDate)
- **Theo dõi bữa ăn đã lên lịch**: Kết nối với scheduled meals

### 3. Quản Lý Bữa Ăn Đã Lên Lịch (Scheduled Meals)
- **3 trạng thái**: SCHEDULED, COMPLETED, CANCELLED
- **Theo dõi tiến trình**: Cập nhật trạng thái bữa ăn
- **Thống kê tiêu thụ**: Xem lịch sử và calories thực tế

### 4. Thống Kê Calories Tiêu Thụ
- **Thống kê theo ngày**: Calories tiêu thụ hàng ngày
- **Thống kê theo tuần**: 7 ngày liên tiếp
- **Thống kê theo tháng**: Toàn bộ tháng
- **Khoảng thời gian tùy chỉnh**: Flexible date range

## MealType và MealStatus

### Meal Types
| Loại Bữa Ăn | Mô Tả |
|--------------|-------|
| BREAKFAST | Bữa sáng |
| LUNCH | Bữa trưa |
| DINNER | Bữa tối |
| SNACK | Bữa phụ/Đồ ăn vặt |

### Meal Status
| Trạng Thái | Mô Tả |
|------------|-------|
| SCHEDULED | Đã lên lịch |
| COMPLETED | Đã hoàn thành |
| CANCELLED | Đã hủy |

**Công thức tính calories:** Meal Calories = Sum(Food Calories)

## Cấu Hình

### Cơ Sở Dữ Liệu
```properties
spring.datasource.url=jdbc:mysql://localhost:3310/meal_db
spring.datasource.username=root
spring.datasource.password=secret
```

### Service Discovery
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

## API Endpoints

### Meal Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/meals` | Tạo bữa ăn mới (auto-calculate calories) |
| GET | `/meals/{id}` | Lấy bữa ăn theo ID |
| GET | `/meals` | Lấy tất cả bữa ăn |
| GET | `/meals/type/{type}` | Lấy bữa ăn theo loại |
| PUT | `/meals/{id}` | Cập nhật bữa ăn |
| DELETE | `/meals/{id}` | Xóa bữa ăn |

### Schedule Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/meals/schedules` | Tạo lịch ăn mới |
| GET | `/meals/schedules/{id}` | Lấy lịch ăn theo ID |
| GET | `/meals/schedules/user/{userId}` | Lấy lịch ăn của user |
| GET | `/meals/schedules/user/{userId}/date-range` | Lấy lịch theo khoảng thời gian |
| PUT | `/meals/schedules/{id}` | Cập nhật lịch ăn |
| DELETE | `/meals/schedules/{id}` | Xóa lịch ăn |

### Scheduled Meal Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/meals/scheduled-meals` | Tạo bữa ăn đã lên lịch |
| GET | `/meals/scheduled-meals/{id}` | Lấy bữa ăn theo ID |
| GET | `/meals/scheduled-meals/schedule/{scheduleId}` | Lấy bữa ăn theo lịch |
| GET | `/meals/scheduled-meals/user/{userId}` | Lấy bữa ăn của user |
| GET | `/meals/scheduled-meals/user/{userId}/status/{status}` | Lấy theo trạng thái |
| PUT | `/meals/scheduled-meals/{id}` | Cập nhật bữa ăn |
| PATCH | `/meals/scheduled-meals/{id}/status/{status}` | Cập nhật trạng thái |
| DELETE | `/meals/scheduled-meals/{id}` | Xóa bữa ăn |

### Calorie Statistics
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/meals/calories/daily/{userId}?date={date}` | Thống kê theo ngày |
| GET | `/meals/calories/weekly/{userId}?startDate={date}` | Thống kê theo tuần |
| GET | `/meals/calories/monthly/{userId}?year={year}&month={month}` | Thống kê theo tháng |
| GET | `/meals/calories/range/{userId}?startDate={start}&endDate={end}` | Thống kê tùy chỉnh |

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
docker run --name meal-mysql -p 3310:3306 \
  -e MYSQL_ROOT_PASSWORD=secret \
  -e MYSQL_DATABASE=meal_db \
  -d mysql:8.0
```

### 2. Khởi Chạy Service
```bash
cd mealservice
mvn spring-boot:run
```

## Postman Test Cases

### Environment Variables
```json
{
  "gateway_url": "http://localhost:8080",
  "meal_url": "http://localhost:8008",
  "user_id": "testuser123",
  "access_token": "your_jwt_token"
}
```

### 1. Tạo Bữa Ăn với Auto-Calculate Calories
```json
POST {{gateway_url}}/meals
Authorization: Bearer {{access_token}}
Content-Type: application/json

{
  "name": "Healthy Breakfast Bowl",
  "description": "Nutritious breakfast with automatic calorie calculation",
  "type": "BREAKFAST",
  "foods": [
    {
      "name": "Rolled Oats",
      "description": "Organic whole grain oats",
      "calories": 150
    },
    {
      "name": "Fresh Blueberries",
      "description": "Antioxidant-rich berries",
      "calories": 85
    },
    {
      "name": "Almonds",
      "description": "Raw unsalted almonds",
      "calories": 165
    }
  ]
}
```

### 2. Tạo Bữa Ăn Đơn Giản
```json
POST {{gateway_url}}/meals
Authorization: Bearer {{access_token}}
Content-Type: application/json

{
  "name": "Protein Shake",
  "description": "Quick protein drink",
  "calories": 200,
  "type": "SNACK"
}
```

### 3. Lấy Bữa Ăn Theo Loại
```json
GET {{gateway_url}}/meals/type/BREAKFAST
Authorization: Bearer {{access_token}}
```

### 4. Cập Nhật Bữa Ăn (Auto-Recalculate)
```json
PUT {{gateway_url}}/meals/1
Authorization: Bearer {{access_token}}
Content-Type: application/json

{
  "name": "Enhanced Breakfast Bowl",
  "description": "Improved breakfast with extra nutrition",
  "type": "BREAKFAST",
  "foods": [
    {
      "name": "Rolled Oats",
      "calories": 150
    },
    {
      "name": "Fresh Blueberries",
      "calories": 85
    },
    {
      "name": "Greek Yogurt",
      "description": "High protein yogurt",
      "calories": 100
    }
  ]
}
```

### 5. Tạo Lịch Ăn Uống
```json
POST {{gateway_url}}/meals/schedules
Authorization: Bearer {{access_token}}
Content-Type: application/json

{
  "userId": "{{user_id}}",
  "name": "Weekly Meal Plan",
  "description": "Healthy meal plan for the week",
  "startDate": "2024-01-15",
  "endDate": "2024-01-21"
}
```

### 6. Lên Lịch Bữa Ăn
```json
POST {{gateway_url}}/meals/scheduled-meals
Authorization: Bearer {{access_token}}
Content-Type: application/json

{
  "scheduleId": 1,
  "mealId": 1,
  "scheduledDateTime": "2024-01-15T08:00:00",
  "status": "SCHEDULED",
  "notes": "Breakfast at home"
}
```

### 7. Cập Nhật Trạng Thái Bữa Ăn
```json
PATCH {{gateway_url}}/meals/scheduled-meals/1/status/COMPLETED
Authorization: Bearer {{access_token}}
```

### 8. Thống Kê Calories Theo Ngày
```json
GET {{gateway_url}}/meals/calories/daily/{{user_id}}?date=2024-01-15
Authorization: Bearer {{access_token}}
```

### 9. Thống Kê Calories Theo Tuần
```json
GET {{gateway_url}}/meals/calories/weekly/{{user_id}}?startDate=2024-01-15
Authorization: Bearer {{access_token}}
```

### 10. Thống Kê Calories Theo Tháng
```json
GET {{gateway_url}}/meals/calories/monthly/{{user_id}}?year=2024&month=1
Authorization: Bearer {{access_token}}
```

### 11. Thống Kê Calories Tùy Chỉnh
```json
GET {{gateway_url}}/meals/calories/range/{{user_id}}?startDate=2024-01-01&endDate=2024-01-31
Authorization: Bearer {{access_token}}
```

### 12. Lấy Lịch Theo Khoảng Thời Gian
```json
GET {{gateway_url}}/meals/schedules/user/{{user_id}}/date-range?startDate=2024-01-01&endDate=2024-01-31
Authorization: Bearer {{access_token}}
```

### 13. Lấy Bữa Ăn Theo Trạng Thái
```json
GET {{gateway_url}}/meals/scheduled-meals/user/{{user_id}}/status/COMPLETED?startDateTime=2024-01-01T00:00:00&endDateTime=2024-01-31T23:59:59
Authorization: Bearer {{access_token}}
```

### 14. Tạo Bữa Tối Phức Tạp
```json
POST {{gateway_url}}/meals
Authorization: Bearer {{access_token}}
Content-Type: application/json

{
  "name": "Balanced Dinner",
  "description": "Well-balanced evening meal",
  "type": "DINNER",
  "foods": [
    {
      "name": "Grilled Chicken Breast",
      "description": "Lean protein source",
      "calories": 230
    },
    {
      "name": "Brown Rice",
      "description": "Complex carbohydrates",
      "calories": 110
    },
    {
      "name": "Steamed Broccoli",
      "description": "Vitamin-rich vegetables",
      "calories": 55
    }
  ]
}
```

### 15. Xóa Bữa Ăn
```json
DELETE {{gateway_url}}/meals/1
Authorization: Bearer {{access_token}}
```

## Lưu Ý

- **Auto-calculation**: Calories được tính tự động từ foods, không cần manual input
- **Data consistency**: JPA lifecycle hooks đảm bảo calories accuracy
- **Performance**: Optimized queries cho thống kê nhanh chóng
- **API Gateway**: Service hoạt động sau lớp API Gateway
- **Authentication**: Xác thực được xử lý ở tầng gateway
- **User ID**: Được truyền từ gateway sau khi xác thực thành công
- **DateTime Format**: Sử dụng ISO-8601 format cho tất cả datetime fields
- **Status Enum**: Các giá trị status phải match chính xác với MealStatus enum
- **Required Fields**: Tất cả fields có annotation @NotNull hoặc @NotBlank là bắt buộc