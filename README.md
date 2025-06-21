# Health App Backend - Microservices System

## Tổng Quan Hệ Thống

Hệ thống backend Health App được xây dựng theo kiến trúc microservices hiện đại, bao gồm **6 services chính** với các tính năng AI-powered và tính toán khoa học cho sức khỏe và dinh dưỡng.

**🏗️ Kiến Trúc:** Microservices với Service Discovery  
**🔒 Bảo Mật:** JWT Authentication + Redis Blacklist  
**📊 Analytics:** AI-powered health calculations với MET values  
**⚡ Performance:** Auto-calculation engines + Batch optimization

## 🎯 Microservices Overview

| Service | Port | Database | Core Features |
|---------|------|----------|---------------|
| **API Gateway** | 8080 | - | Routing, Auth, Rate Limiting, Circuit Breaker |
| **AuthService** | 8005 | auth_db (3307) | JWT Authentication, Token Blacklist, User Registration |
| **UserService** | 8006 | user_db (3308) | Profile Management, Activity Levels, Health Data |
| **WorkoutService** | 8007 | workout_db (3309) | **MET-based Calorie Calculation**, Exercise Management |
| **MealService** | 8008 | meal_db (3310) | **Auto-sync Calories**, Nutrition Tracking |
| **AnalystService** | 8009 | analyst_db (3311) | **AI Health Analytics**, BMI/BMR/TDEE Calculations |

## 🏗️ System Architecture

```
                    ┌───────────────────────────┐
                    │       API Gateway         │
                    │      (Port: 8080)         │
                    │    • JWT Authentication   │
                    │    • Rate Limiting        │
                    │    • Circuit Breaker      │
                    │    • Request Routing      │
                    └─────────────┬─────────────┘
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
│ :3307 │    │ :3308 │    │   :3309   │    │ :3310 │    │ :3311 │
└───────┘    └───────┘    └───────────┘    └───────┘    └───────┘
                                │
                          ┌─────▼─────┐
                          │   Redis   │
                          │ (Blacklist)│
                          │   :6379   │
                          └───────────┘
```

## 📊 Service Details

### 🌐 API Gateway (Port 8080)
**Role:** Central Entry Point & Security Layer

**Tính năng:**
- **Định tuyến thông minh**: Routes đến đúng microservice
- **JWT Authentication**: Xác thực tập trung cho tất cả protected endpoints
- **Token Blacklist**: Kiểm tra token bị vô hiệu hóa qua Redis
- **Rate Limiting**: 100 requests/second per client
- **Circuit Breaker**: Ngăn chặn lỗi lan truyền
- **CORS Support**: Cross-origin resource sharing

**Routing Rules:**
- `/auth/**` → AuthService (Public)
- `/users/**` → UserService (Auth Required)
- `/workouts/**` → WorkoutService (Auth Required)
- `/meals/**` → MealService (Auth Required)
- `/analytics/**` → AnalystService (Auth Required)

### 🔐 AuthService (Port 8005)
**Role:** Authentication & Authorization

**Tính năng:**
- **User Registration**: Tạo tài khoản với validation
- **JWT Authentication**: Secure login với token generation
- **Password Management**: Đổi mật khẩu với token refresh
- **Token Blacklist**: Logout với Redis-based token revocation
- **User Sync**: Tự động tạo profile trong UserService

**Database:** auth_db (MySQL port 3307)

### 👤 UserService (Port 8006)
**Role:** User Profile & Health Data Management

**Tính năng:**
- **Profile Management**: Thông tin cá nhân (fullName, email, gender, birthDate)
- **Health Data**: Thông số sức khỏe (weight, height)
- **Activity Levels**: 5 levels từ SEDENTARY (1.2) đến EXTRA_ACTIVE (1.9)
- **Security**: Bảo vệ email/password không thể cập nhật
- **Integration**: Đồng bộ với AuthService và AnalystService

**Database:** user_db (MySQL port 3308)

### 🏃 WorkoutService (Port 8007)
**Role:** Workout Management & MET-based Calorie Calculation

**🔥 Advanced Features:**
- **MET-based Calculation**: Scientific calorie calculation
  - Formula: `Calories = MET × weight(kg) × duration(hours)`
- **7 Workout Types**: CARDIO (7.0), STRENGTH (4.5), HIIT (9.0), CROSSFIT (9.0), YOGA (2.8), PILATES (3.5), FLEXIBILITY (2.5)
- **Exercise Management**: Sets, reps, duration tracking
- **Schedule Management**: Workout planning với 5 statuses
- **Statistics**: Daily/Weekly/Monthly calorie burn analytics
- **Auto-calculation**: JPA lifecycle hooks ensure accuracy

**Database:** workout_db (MySQL port 3309)

### 🍽️ MealService (Port 8008)
**Role:** Nutrition Management & Auto-sync Calories

**🔥 Advanced Features:**
- **Auto-sync Calories**: `Meal Calories = Sum(Food Calories)`
- **4 Meal Types**: BREAKFAST, LUNCH, DINNER, SNACK
- **3 Meal Status**: SCHEDULED, COMPLETED, CANCELLED
- **Food Management**: Individual food items với calorie tracking
- **Schedule Planning**: Meal scheduling với date ranges
- **Statistics**: Daily/Weekly/Monthly calorie intake analytics
- **Data Consistency**: JPA lifecycle hooks đảm bảo accuracy

**Database:** meal_db (MySQL port 3310)

### 📊 AnalystService (Port 8009)
**Role:** AI-Powered Health Analytics

**🧠 AI Features:**
- **Health Analytics**: BMI, BMR, TDEE calculations
  - BMR: Mifflin-St Jeor equation
  - TDEE: Activity factor-based calculation
- **Activity Analysis**: AI-based workout pattern analysis
- **Dual Strategy**: Manual vs Workout-based TDEE calculation
- **Calorie Analysis**: Intake vs burn comparison
- **Smart Recommendations**: Health advice dựa trên BMI categories
- **Integration**: Kết nối UserService và WorkoutService cho data

**Database:** analyst_db (MySQL port 3311)

## 🔄 System Workflows

### Authentication Flow
```
1. Client → API Gateway (/auth/signup hoặc /auth/login)
2. API Gateway → AuthService (validate credentials)
3. AuthService → UserService (create/sync profile)
4. AuthService ← UserService (profile confirmation)
5. Client ← API Gateway (JWT token + user info)
```

### Business Logic Flow
```
1. Client → API Gateway (với JWT token)
2. API Gateway validates JWT + checks Redis blacklist
3. API Gateway → Target Service
4. Target Service processes + auto-calculations
5. Client ← API Gateway (response data)
```

### Inter-Service Communication
```
- AuthService ↔ UserService (user profile sync)
- AnalystService → UserService (get health data)
- AnalystService → WorkoutService (get activity data)
- WorkoutService → UserService (get weight for MET calculation)
```

## ⚙️ Tech Stack

### Core Technologies
- **Framework**: Spring Boot 3.2.3
- **Language**: Java 21
- **Security**: Spring Security + JWT (JJWT 0.11.5)
- **Database**: MySQL 8.0 (per service)
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

## 🚀 Installation & Setup

### Prerequisites
- **Java 21** (JDK)
- **Maven 3.9+**
- **Docker & Docker Compose**
- **MySQL 8.0+**
- **Redis 6.0+**

### Quick Start

#### 1. Infrastructure Setup
```bash
# Redis for token blacklist
docker run --name redis -p 6379:6379 -d redis

# MySQL databases for each service
docker run --name auth-mysql -p 3307:3306 -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=auth_db -d mysql:8.0
docker run --name user-mysql -p 3308:3306 -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=user_db -d mysql:8.0
docker run --name workout-mysql -p 3309:3306 -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=workout_db -d mysql:8.0
docker run --name meal-mysql -p 3310:3306 -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=meal_db -d mysql:8.0
docker run --name analyst-mysql -p 3311:3306 -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=analyst_db -d mysql:8.0
```

#### 2. Service Discovery (Eureka Server)
```bash
# Tạo Eureka Server nếu chưa có
mkdir discoveryserver && cd discoveryserver

# Khởi chạy Eureka Server
mvn spring-boot:run
# Dashboard: http://localhost:8761
```

#### 3. Start Services (theo thứ tự)
```bash
# 1. API Gateway
cd apiservice && mvn spring-boot:run &

# 2. Core Services (có thể song song)
cd authservice && mvn spring-boot:run &
cd userservice && mvn spring-boot:run &
cd workoutservice && mvn spring-boot:run &
cd mealservice && mvn spring-boot:run &
cd analystservice && mvn spring-boot:run &
```

#### 4. Health Check
```bash
# Check Eureka Dashboard
curl http://localhost:8761

# Check API Gateway
curl http://localhost:8080/actuator/health

# Check all services registered
curl http://localhost:8761/eureka/apps
```

## 🧪 API Testing với Postman

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

### Complete Test Workflow

#### 1. Authentication Tests
```json
// User Registration
POST {{gateway_url}}/auth/signup
{
  "fullName": "Test User",
  "email": "{{user_email}}",
  "password": "{{user_password}}"
}

// User Login - Save token
POST {{gateway_url}}/auth/login
{
  "email": "{{user_email}}",
  "password": "{{user_password}}"
}
```

#### 2. User Profile Tests
```json
// Get User Profile
GET {{gateway_url}}/users/{{user_id}}
Authorization: Bearer {{access_token}}

// Update User Profile
PUT {{gateway_url}}/users/{{user_id}}
Authorization: Bearer {{access_token}}
{
  "fullName": "Updated Name",
  "gender": "MALE",
  "birthDate": "1990-01-15",
  "weight": 70.0,
  "height": 175.0
}

// Set Activity Level
PUT {{gateway_url}}/users/{{user_id}}/activity-level?activityLevel=MODERATELY_ACTIVE
Authorization: Bearer {{access_token}}
```

#### 3. Workout Tests (MET-based Calculation)
```json
// Create HIIT Workout (9.0 MET)
POST {{gateway_url}}/workouts
Authorization: Bearer {{access_token}}
{
  "name": "Morning HIIT",
  "type": "HIIT",
  "durationMinutes": 30,
  "exercises": [
    {
      "name": "Burpees",
      "sets": 3,
      "reps": 10,
      "durationSeconds": 300
    }
  ]
}

// Get Daily Calorie Burn Stats
GET {{gateway_url}}/workouts/calories-burned/daily/{{user_id}}?date=2024-01-15
Authorization: Bearer {{access_token}}
```

#### 4. Meal Tests (Auto-sync Calories)
```json
// Create Breakfast (Auto-calculate calories)
POST {{gateway_url}}/meals
Authorization: Bearer {{access_token}}
{
  "name": "Healthy Breakfast",
  "type": "BREAKFAST",
  "foods": [
    {
      "name": "Oatmeal",
      "calories": 150
    },
    {
      "name": "Banana",
      "calories": 105
    }
  ]
}

// Get Daily Calorie Intake
GET {{gateway_url}}/meals/calories/daily/{{user_id}}?date=2024-01-15
Authorization: Bearer {{access_token}}
```

#### 5. Analytics Tests (AI-Powered)
```json
// Get Complete Health Metrics
GET {{gateway_url}}/analytics/health-analytics/health-metrics/{{user_id}}
Authorization: Bearer {{access_token}}

// Get TDEE with Strategy (Auto-select best method)
GET {{gateway_url}}/analytics/health-analytics/tdee/{{user_id}}/strategy
Authorization: Bearer {{access_token}}

// Activity Analysis (AI-based)
GET {{gateway_url}}/analytics/activity-analysis/user/{{user_id}}
Authorization: Bearer {{access_token}}
```

## 📊 Monitoring & Observability

### Health Check Endpoints
```bash
# Service Discovery
curl http://localhost:8761

# API Gateway Health
curl http://localhost:8080/actuator/health

# Individual Services
curl http://localhost:8005/actuator/health  # AuthService
curl http://localhost:8006/actuator/health  # UserService  
curl http://localhost:8007/actuator/health  # WorkoutService
curl http://localhost:8008/actuator/health  # MealService
curl http://localhost:8009/actuator/health  # AnalystService
```

### Metrics & Monitoring
- **Eureka Dashboard**: http://localhost:8761
- **Actuator Metrics**: `/actuator/metrics` trên mỗi service
- **Health Indicators**: `/actuator/health` với detailed status
- **Prometheus**: `/actuator/prometheus` (if enabled)

## 🎯 Key Benefits

### 🔥 Business Value
- **Scientific Accuracy**: MET-based workout calories + auto-sync meal calories
- **AI-Powered Insights**: Smart health recommendations và TDEE calculations
- **Real-time Updates**: Auto-calculation engines ensure data consistency
- **Comprehensive Tracking**: Complete health ecosystem từ nutrition đến fitness

### ⚡ Technical Excellence
- **Microservice Architecture**: Independent scaling và deployment
- **Enterprise Security**: JWT + Redis blacklist + rate limiting
- **Performance Optimization**: Batch queries + lifecycle hooks
- **Fault Tolerance**: Circuit breakers + service discovery

### 📈 Scalability & Reliability
- **Service Isolation**: Each service có own database và responsibilities
- **Load Distribution**: API Gateway routes efficiently
- **Data Consistency**: Auto-calculation engines prevent manual errors
- **Monitoring Ready**: Comprehensive health checks và metrics

## 📚 Documentation

### Individual Service READMEs
- [API Gateway Documentation](./apiservice/README.md)
- [AuthService Documentation](./authservice/README.md)
- [UserService Documentation](./userservice/README.md)
- [WorkoutService Documentation](./workoutservice/README.md)
- [MealService Documentation](./mealservice/README.md)
- [AnalystService Documentation](./analystservice/README.md)
