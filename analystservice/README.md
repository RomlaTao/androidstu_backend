# Analyst Service

## Chức năng chính

**Analyst Service** là microservice phân tích sức khỏe và dinh dưỡng, cung cấp 3 nhóm chức năng chính:

### 🏥 Health Analytics
- Tính toán BMI, BMR, TDEE dựa trên thông tin cơ thể
- Đưa ra khuyến nghị sức khỏe
- Hỗ trợ nhiều phương pháp tính TDEE (manual, workout-based, user-input)

### 🏃 Activity Analysis  
- Phân tích hoạt động dựa trên dữ liệu workout thực tế
- Tính toán activity factor từ lịch sử tập luyện
- Cung cấp TDEE chính xác hơn dựa trên workout patterns

### 🍎 Calorie Analysis
- Phân tích lượng calories nạp vào theo ngày/tuần/tháng
- So sánh calories vs TDEE để hỗ trợ quản lý cân nặng
- Theo dõi xu hướng dinh dưỡng và đưa ra insights

## Cấu hình

### Database & Dependencies
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3311/analyst_db
spring.datasource.username=root
spring.datasource.password=secret

# Server
server.port=8009

# External Services
services.userservice.url=http://localhost:8006
services.workoutservice.url=http://localhost:8008

# Eureka Discovery
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

### Required Services
- **User Service** (port 8006): Lấy thông tin user
- **Workout Service** (port 8008): Lấy dữ liệu workout
- **Meal Service**: Lấy dữ liệu calories (cho calorie analysis)
- **Eureka Server** (port 8761): Service discovery

### Tech Stack
- Spring Boot 3.2.3
- Spring Security + JWT
- MySQL Database
- WebFlux (Reactive)
- SpringDoc OpenAPI

## API Test Cases (Postman)

### Environment Variables
```json
{
  "base_url": "http://localhost:8009",
  "user_id": "1",
  "test_weight": "70.0",
  "test_height": "175.0",
  "test_age": "25"
}
```

### 🏥 Health Analytics Tests

#### 1. Get Health Metrics
```json
{
  "method": "GET",
  "url": "{{base_url}}/analytics/health-analytics/health-metrics/{{user_id}}"
}
```

#### 2. Calculate TDEE with Activity Level
```json
{
  "method": "GET", 
  "url": "{{base_url}}/analytics/health-analytics/tdee/{{user_id}}?activityLevel=MODERATELY_ACTIVE"
}
```

#### 3. Calculate BMI Direct
```json
{
  "method": "POST",
  "url": "{{base_url}}/analytics/health-analytics/calculate-bmi",
  "headers": {"Content-Type": "application/x-www-form-urlencoded"},
  "body": {
    "weight": "{{test_weight}}",
    "height": "{{test_height}}"
  }
}
```

#### 4. Calculate BMR Direct
```json
{
  "method": "POST",
  "url": "{{base_url}}/analytics/health-analytics/calculate-bmr",
  "headers": {"Content-Type": "application/x-www-form-urlencoded"},
  "body": {
    "weight": "{{test_weight}}",
    "height": "{{test_height}}",
    "age": "{{test_age}}",
    "gender": "MALE"
  }
}
```

#### 5. TDEE with Strategy (Auto-select method)
```json
{
  "method": "GET",
  "url": "{{base_url}}/analytics/health-analytics/tdee/{{user_id}}/strategy"
}
```

#### 6. TDEE User Input
```json
{
  "method": "GET",
  "url": "{{base_url}}/analytics/health-analytics/tdee/{{user_id}}/user-input"
}
```

### 🏃 Activity Analysis Tests

#### 1. Analyze User Activity
```json
{
  "method": "GET",
  "url": "{{base_url}}/analytics/activity-analysis/user/{{user_id}}"
}
```

#### 2. Workout-based TDEE
```json
{
  "method": "GET",
  "url": "{{base_url}}/analytics/activity-analysis/user/{{user_id}}/workout-based-tdee"
}
```

### 🍎 Calorie Analysis Tests

#### 1. Daily Calories
```json
{
  "method": "GET",
  "url": "{{base_url}}/analytics/calorie-analysis/daily/{{user_id}}?date=2023-12-07"
}
```

#### 2. Detailed Daily Calories
```json
{
  "method": "GET",
  "url": "{{base_url}}/analytics/calorie-analysis/daily/{{user_id}}/detailed?date=2023-12-07"
}
```

#### 3. Weekly Calories
```json
{
  "method": "GET",
  "url": "{{base_url}}/analytics/calorie-analysis/weekly/{{user_id}}?startDate=2023-12-01"
}
```

#### 4. Calories vs TDEE Analysis
```json
{
  "method": "GET",
  "url": "{{base_url}}/analytics/calorie-analysis/vs-tdee/{{user_id}}?date=2023-12-07"
}
```

#### 5. Calorie Trends
```json
{
  "method": "GET",
  "url": "{{base_url}}/analytics/calorie-analysis/trends/{{user_id}}?startDate=2023-12-01&endDate=2023-12-07"
}
```

### Activity Levels
- `SEDENTARY` (1.2): Ít hoặc không tập
- `LIGHTLY_ACTIVE` (1.375): Tập nhẹ 1-3 ngày/tuần  
- `MODERATELY_ACTIVE` (1.55): Tập vừa 3-5 ngày/tuần
- `VERY_ACTIVE` (1.725): Tập nặng 6-7 ngày/tuần
- `EXTRA_ACTIVE` (1.9): Tập rất nặng + công việc thể lực

### Test Validation Scripts
```javascript
// Validate BMI calculation
pm.test("BMI is correct", function () {
    const bmi = pm.response.json();
    pm.expect(bmi).to.be.approximately(22.86, 0.01);
});

// Validate response structure
pm.test("Health metrics structure", function () {
    const response = pm.response.json();
    pm.expect(response).to.have.property('userId');
    pm.expect(response).to.have.property('bmi');
    pm.expect(response).to.have.property('bmr');
});

// Performance test
pm.test("Response time < 2s", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});
```

## Documentation
- **Swagger UI**: http://localhost:8009/swagger-ui.html
- **API Docs**: http://localhost:8009/v3/api-docs 