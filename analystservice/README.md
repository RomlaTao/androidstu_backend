# Analyst Service

## Tổng quan

**Analyst Service** là microservice chuyên trách phân tích và tính toán các chỉ số sức khỏe cho ứng dụng fitness. Service này cung cấp hai loại phân tích chính:

1. **Health Analytics** - Tính toán các chỉ số sức khỏe cơ bản (BMI, BMR, TDEE)
2. **Activity Analysis** - Phân tích hoạt động dựa trên dữ liệu workout thực tế

## Kiến trúc và Thiết kế

### Tại sao có 2 Controllers riêng biệt?

#### 🏥 HealthAnalyticsController
- **Mục đích**: Tính toán các chỉ số sức khỏe cơ bản
- **Dữ liệu đầu vào**: Thông tin user tĩnh (cân nặng, chiều cao, tuổi, giới tính)
- **Phương pháp**: Sử dụng công thức toán học chuẩn (Mifflin-St Jeor)
- **Use case**: User mới, tính toán nhanh, estimation ban đầu

#### 🏃 ActivityAnalysisController  
- **Mục đích**: Phân tích hoạt động và tính toán TDEE chính xác
- **Dữ liệu đầu vào**: Workout data thực tế trong 7 ngày qua
- **Phương pháp**: Machine learning approach, phân tích pattern
- **Use case**: User có lịch sử, personalized calculation

### Thiết kế này mang lại:
- ✅ **Separation of Concerns**: Mỗi controller có trách nhiệm rõ ràng
- ✅ **Scalability**: Dễ mở rộng từng phần độc lập
- ✅ **Maintainability**: Code dễ bảo trì và test
- ✅ **Performance**: Tối ưu cho từng loại calculation

## API Endpoints

### 🏥 Health Analytics Controller

#### 1. Get Health Metrics by User ID

**Endpoint:** `GET /analytics/health-analytics/health-metrics/{userId}`

**Description:** Tính toán BMI, BMR và cung cấp health recommendations cho user

**Postman Test:**
```json
{
  "method": "GET",
  "url": "http://localhost:8080/analytics/health-analytics/health-metrics/1",
  "headers": {}
}
```

**Expected Response:**
```json
{
  "userId": 1,
  "fullName": "Nguyễn Văn A",
  "height": 175.0,
  "weight": 70.0,
  "age": 25,
  "gender": "MALE",
  "bmi": 22.86,
  "bmiCategory": "Normal weight",
  "bmr": 1750.0,
  "bmrFormula": "Mifflin-St Jeor Equation",
  "calculatedAt": "2023-12-07T10:30:00",
  "recommendations": [
    "Your BMI is in the normal range",
    "Maintain your current weight with balanced diet"
  ]
}
```

#### 2. Get Health Metrics by Email

**Endpoint:** `GET /analytics/health-analytics/health-metrics/email/{email}`

**Postman Test:**
```json
{
  "method": "GET", 
  "url": "http://localhost:8080/analytics/health-analytics/health-metrics/email/user@example.com",
  "headers": {}
}
```

#### 3. Calculate TDEE with Activity Level

**Endpoint:** `GET /analytics/health-analytics/tdee/{userId}`

**Activity Levels:** `SEDENTARY`, `LIGHTLY_ACTIVE`, `MODERATELY_ACTIVE`, `VERY_ACTIVE`, `EXTRA_ACTIVE`

**Postman Test:**
```json
{
  "method": "GET",
  "url": "http://localhost:8080/analytics/health-analytics/tdee/1",
  "headers": {}
}
```

**Expected Response:**
```json
{
  "userId": 1,
  "fullName": "Nguyễn Văn A",
  "bmr": 1750.0,
  "activityLevel": "Moderately Active",
  "activityFactor": 1.55,
  "tdee": 2712.5,
  "maintenanceCalories": 2712.5,
  "weightLossCalories": 2212.5,
  "weightGainCalories": 3212.5,
  "calculationMethod": "Mifflin-St Jeor",
  "calculatedAt": "2023-12-07T10:30:00"
}
```

#### 4. Calculate BMI Directly

**Endpoint:** `POST /analytics/health-analytics/calculate-bmi`

**Postman Test:**
```json
{
  "method": "POST",
  "url": "http://localhost:8083/analytics/health-analytics/calculate-bmi",
  "headers": {
    "Content-Type": "application/x-www-form-urlencoded"
  },
  "body": {
    "mode": "urlencoded",
    "urlencoded": [
      {"key": "weight", "value": "70.0"},
      {"key": "height", "value": "175.0"}
    ]
  }
}
```

**Expected Response:**
```json
22.86
```

#### 5. Calculate BMR Directly

**Endpoint:** `POST /analytics/health-analytics/calculate-bmr`

**Postman Test:**
```json
{
  "method": "POST",
  "url": "http://localhost:8083/analytics/health-analytics/calculate-bmr",
  "headers": {
    "Content-Type": "application/x-www-form-urlencoded"
  },
  "body": {
    "mode": "urlencoded", 
    "urlencoded": [
      {"key": "weight", "value": "70.0"},
      {"key": "height", "value": "175.0"},
      {"key": "age", "value": "25"},
      {"key": "gender", "value": "MALE"}
    ]
  }
}
```

**Expected Response:**
```json
1750.23
```

#### 6. Calculate TDEE with Strategy

**Endpoint:** `GET /analytics/health-analytics/tdee/{userId}/strategy`

**Description:** Tự động chọn phương pháp tính TDEE dựa trên profile user

**Postman Test:**
```json
{
  "method": "GET",
  "url": "http://localhost:8083/analytics/health-analytics/tdee/1/strategy",
  "headers": {}
}
```

#### 7. Calculate TDEE Manual

**Endpoint:** `POST /analytics/health-analytics/tdee/{userId}/manual`

**Postman Test:**
```json
{
  "method": "POST",
  "url": "http://localhost:8083/analytics/health-analytics/tdee/1/manual",
  "headers": {
    "Content-Type": "application/x-www-form-urlencoded"
  },
  "body": {
    "mode": "urlencoded",
    "urlencoded": [
      {"key": "activityLevel", "value": "VERY_ACTIVE"}
    ]
  }
}
```

**Expected Response:**
```json
{
  "userId": 1,
  "fullName": "Nguyễn Văn A",
  "bmr": 1750.0,
  "activityLevel": "Very Active",
  "activityFactor": 1.725,
  "tdee": 3018.75,
  "maintenanceCalories": 3018.75,
  "weightLossCalories": 2518.75,
  "weightGainCalories": 3518.75,
  "calculationMethod": "Manual Activity Level",
  "methodDescription": "Dựa trên mức độ hoạt động bạn đã chọn",
  "calculatedAt": "2023-12-07T10:30:00"
}
```

### 🏃 Activity Analysis Controller

#### 1. Analyze User Activity

**Endpoint:** `GET /analytics/activity-analysis/user/{userId}`

**Description:** Phân tích workout data 7 ngày qua và tính activity factor

**Postman Test:**
```json
{
  "method": "GET",
  "url": "http://localhost:8083/analytics/activity-analysis/user/1",
  "headers": {}
}
```

**Expected Response:**
```json
{
  "userId": 1,
  "analysisStartDate": "2023-11-30",
  "analysisEndDate": "2023-12-07",
  "totalWorkoutDays": 4,
  "calculatedActivityFactor": 1.55,
  "activityLevelDescription": "Moderately Active",
  "workoutSummary": {
    "totalSessions": 4,
    "avgDuration": 45,
    "totalCaloriesBurned": 1200,
    "workoutTypes": ["Cardio", "Strength", "Yoga"]
  }
}
```

#### 2. Calculate Workout-Based TDEE

**Endpoint:** `GET /analytics/activity-analysis/user/{userId}/workout-based-tdee`

**Description:** Tính TDEE dựa trên dữ liệu workout thực tế

**Postman Test:**
```json
{
  "method": "GET",
  "url": "http://localhost:8083/analytics/activity-analysis/user/1/workout-based-tdee",
  "headers": {}
}
```

**Expected Response:**
```json
{
  "userId": 1,
  "fullName": "Nguyễn Văn A",
  "bmr": 1750.0,
  "activityLevel": "Workout-based (Moderately Active)",
  "activityFactor": 1.55,
  "tdee": 2712.5,
  "maintenanceCalories": 2712.5,
  "weightLossCalories": 2212.5,
  "weightGainCalories": 3212.5,
  "calculationMethod": "Workout-based Analysis",
  "methodDescription": "Dựa trên lịch tập thực tế của bạn trong 7 ngày qua",
  "accuracyScore": 0.87,
  "dataQuality": "High",
  "calculatedAt": "2023-12-07T10:30:00"
}
```

## Activity Level Mapping

| Workout Days/Week | Activity Factor | Level | Description |
|-------------------|-----------------|-------|-------------|
| 0 | 1.2 | Sedentary | Ít hoặc không tập thể dục |
| 1-2 | 1.375 | Lightly Active | Tập nhẹ 1-3 ngày/tuần |
| 3-4 | 1.55 | Moderately Active | Tập vừa 3-5 ngày/tuần |
| 5-6 | 1.725 | Very Active | Tập nặng 6-7 ngày/tuần |
| 7+ | 1.9 | Extra Active | Tập rất nặng + công việc thể lực |

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2023-12-07T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid input parameters",
  "path": "/analytics/health-analytics/calculate-bmi"
}
```

### 404 Not Found
```json
{
  "timestamp": "2023-12-07T10:30:00",
  "status": 404,
  "error": "Not Found", 
  "message": "User not found with id: 999",
  "path": "/analytics/health-analytics/health-metrics/999"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2023-12-07T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Service temporarily unavailable",
  "path": "/analytics/activity-analysis/user/1"
}
```

## Testing với Postman

### Setup Environment Variables

Tạo Environment trong Postman với các biến:

```json
{
  "base_url": "http://localhost:8083",
  "user_id": "1",
  "user_email": "test@example.com",
  "test_weight": "70.0",
  "test_height": "175.0",
  "test_age": "25",
  "test_gender": "MALE"
}
```

### Test Scenarios

#### 1. Happy Path Tests

**Test BMI Calculation:**
```javascript
// Test script trong Postman
pm.test("BMI calculation is correct", function () {
    const responseJson = pm.response.json();
    // 70kg, 175cm = 70 / (1.75^2) = 22.86
    pm.expect(responseJson).to.be.approximately(22.86, 0.01);
});
```

**Test Health Metrics:**
```javascript
pm.test("Health metrics response is valid", function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson).to.have.property('bmi');
    pm.expect(responseJson).to.have.property('bmr');
    pm.expect(responseJson).to.have.property('bmiCategory');
    pm.expect(responseJson.bmi).to.be.greaterThan(0);
});
```

#### 2. Edge Case Tests

**Test Invalid Weight:**
```json
{
  "method": "POST",
  "url": "{{base_url}}/analytics/health-analytics/calculate-bmi",
  "body": {
    "weight": "0",
    "height": "175.0"
  }
}
```

**Test Non-existent User:**
```json
{
  "method": "GET",
  "url": "{{base_url}}/analytics/health-analytics/health-metrics/99999"
}
```

#### 3. Performance Tests

```javascript
pm.test("Response time is acceptable", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});
```

### Integration Tests

#### Compare Manual vs Workout-based TDEE

**Step 1:** Get Manual TDEE
```json
{
  "method": "GET",
  "url": "{{base_url}}/analytics/health-analytics/tdee/{{user_id}}?activityLevel=MODERATELY_ACTIVE"
}
```

**Step 2:** Get Workout-based TDEE  
```json
{
  "method": "GET",
  "url": "{{base_url}}/analytics/activity-analysis/user/{{user_id}}/workout-based-tdee"
}
```

**Validation:**
```javascript
pm.test("TDEE values are consistent", function () {
    const manualTdee = pm.globals.get("manual_tdee");
    const workoutTdee = pm.response.json().tdee;
    const difference = Math.abs(manualTdee - workoutTdee);
    pm.expect(difference).to.be.below(500); // Difference < 500 calories
});
```

## Dependencies

Service này phụ thuộc vào:
- **User Service**: Để lấy thông tin user
- **Workout Service**: Để lấy dữ liệu workout

## Configuration

**Port:** 8083  
**Database:** Không sử dụng database riêng, consume data từ các services khác  
**Environment:** Spring Boot với WebFlux (Reactive)

## Deployment

### Local Development
```bash
cd analystservice
mvn spring-boot:run
```

### Docker
```bash
docker build -t analyst-service .
docker run -p 8083:8083 analyst-service
```

### Health Check
```bash
curl http://localhost:8083/actuator/health
```

## Monitoring và Logging

- **Health Check**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Logs**: Structured logging với correlation ID
- **Tracing**: Distributed tracing với Sleuth

## Security

- Input validation cho tất cả parameters
- Rate limiting cho public endpoints  
- CORS configuration
- Authentication thông qua API Gateway

## Future Enhancements

1. **Machine Learning**: Improve activity factor prediction
2. **Nutrition Analysis**: Integrate với nutrition data
3. **Advanced Metrics**: Body fat percentage, muscle mass
4. **Caching**: Redis cache cho frequent calculations
5. **Real-time Updates**: WebSocket cho live metrics 