# Health Application Backend Microservices

## Giới thiệu
    Hệ thống backend của ứng dụng Health gồm **6 microservices** chính được thiết kế theo kiến trúc microservice hiện đại:

- **APIService (API Gateway)**: Điểm vào trung tâm cho tất cả client requests, xử lý xác thực tập trung và định tuyến
- **AuthService**: Quản lý đăng ký, đăng nhập, và đăng xuất người dùng  
- **UserService**: Quản lý thông tin người dùng và dữ liệu sức khỏe
- **WorkoutService**: Quản lý bài tập, lịch tập với **tính toán calories tự động từ MET values**
- **MealService**: Quản lý bữa ăn, thực phẩm với **tự động đồng bộ calories**
- **AnalystService**: Phân tích sức khỏe và hoạt động với **AI-powered calculations**

## 🏗️ Kiến trúc Hệ Thống

### Microservices Architecture
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Mobile App    │    │   Web Client    │    │   Admin Panel   │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────▼─────────────┐
                    │      API Gateway         │
                    │     (Port: 8080)         │
                    │   • JWT Authentication   │
                    │   • Rate Limiting        │
                    │   • Circuit Breaker      │
                    │   • Request Routing      │
                    └─────────────┬─────────────┘
                                 │
          ┌──────────────────────┼──────────────────────┌──────────────────────┌──────────────────────┐
          │                      │                      │
    ┌─────▼─────┐         ┌─────▼─────┐         ┌─────▼─────┐
    │ AuthService│         │UserService│         │WorkoutSvc │  
    │ Port: 8005 │         │Port: 8006 │         │Port: 8007 │
    └─────┬─────┘         └─────┬─────┘         └─────┬─────┘
          │                     │                     │
          │               ┌─────▼─────┐         ┌─────▼─────┐
          │               │ MealService│         │AnalystSvc │
          │               │Port: 8008 │         │Port: 8009 │
          │               └───────────┘         └───────────┘
          │
    ┌─────▼─────┐
    │   Redis   │
    │Port: 6379 │
    │(Blacklist)│
    └───────────┘
```

### Technology Stack
- **Framework**: Spring Boot 3.2.3
- **Language**: Java 21  
- **Security**: Spring Security + JWT
- **Database**: MySQL 8.0 (per service)
- **Caching**: Redis (token blacklist)
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Container**: Docker & Docker Compose ready

## 📊 Service Details

| Service | Port | Database | Main Features |
|---------|------|----------|---------------|
| **API Gateway** | 8080 | - | Routing, Auth, Rate Limiting, Circuit Breaker |
| **AuthService** | 8005 | auth_db (3306) | Registration, Login, JWT Management, Token Blacklist |
| **UserService** | 8006 | user_db (3307) | User Profile, Health Data, Personal Information |
| **WorkoutService** | 8007 | workout_db (3309) | **MET-based Calorie Calculation**, Workout Scheduling |
| **MealService** | 8008 | meal_db (3310) | **Auto-sync Calories**, Meal Planning, Nutrition Tracking |
| **AnalystService** | 8009 | - | **AI Health Analytics**, BMI/BMR/TDEE, Activity Analysis |

## 🔥 Key Features

### ⚡ Advanced Calorie Management
- **WorkoutService**: MET-based scientific calorie calculation (CARDIO: 7.0, HIIT: 9.0, YOGA: 2.8, etc.)
- **MealService**: Automatic calorie synchronization (meal calories = sum of food calories)
- **AnalystService**: Personalized TDEE calculation based on actual workout data

### 🧠 AI-Powered Analytics
- **Health Metrics**: BMI, BMR calculation using Mifflin-St Jeor equation
- **Activity Analysis**: Machine learning approach for activity factor calculation
- **Dual Strategy**: Manual vs Workout-based TDEE calculation
- **Smart Recommendations**: Health advice based on user data

### 🔒 Enterprise Security
- **Centralized Authentication**: JWT-based authentication at API Gateway
- **Token Blacklist**: Redis-powered token revocation
- **Rate Limiting**: 100 requests/second protection
- **Circuit Breaker**: Fault tolerance with Resilience4j

### 📈 Performance Optimization
- **Batch Queries**: Optimized database operations
- **Caching Strategy**: Redis for frequently accessed data
- **Microservice Isolation**: Independent scaling per service
- **Service Discovery**: Automatic service registration with Eureka

## Cấu trúc hệ thống

```
health_backend/androidstu_backend/
├── apiservice/        # 🌐 API Gateway Service (Port: 8080)
├── authservice/       # 🔐 Authentication Service (Port: 8005)
├── userservice/       # 👤 User Management Service (Port: 8006)
├── workoutservice/    # 🏃 Workout Management Service (Port: 8007)
├── mealservice/       # 🍽️ Meal Management Service (Port: 8008)
├── analystservice/    # 📊 Health Analytics Service (Port: 8083)
├── discoveryserver/   # 🎯 Eureka Service Discovery Server (Port: 8761)
└── README.md         # 📚 This file
```

## 🔄 Luồng hoạt động

### Authentication Flow
```
1. Client → API Gateway (/auth/signup or /auth/login)
2. API Gateway → AuthService (validate credentials)
3. AuthService → UserService (create/update profile)
4. AuthService ← UserService (profile confirmation)
5. Client ← API Gateway (JWT token + user info)
```

### Business Logic Flow
```
1. Client → API Gateway (with JWT token)
2. API Gateway validates JWT + checks Redis blacklist
3. API Gateway → Target Service (WorkoutService/MealService/etc.)
4. Target Service processes business logic
5. Client ← API Gateway (response data)
```

### Inter-Service Communication
```
- AnalystService → UserService (get user profile data)
- AnalystService → WorkoutService (get workout statistics)
- AuthService → UserService (create user profile)
- WorkoutService ↔ AnalystService (activity data exchange)
```

## Chi tiết từng Service

### 1. 🌐 APIService (API Gateway)

**Port**: 8080 | **Role**: Central Entry Point

**Core Features**:
- **JWT Authentication**: Validates tokens for all protected endpoints
- **Token Blacklist**: Redis-based token revocation checking  
- **Rate Limiting**: 100 requests/second per client
- **Circuit Breaker**: Automatic failover for downstream services
- **Request Routing**: Routes to appropriate microservices
- **CORS Handling**: Cross-origin request management

**Key Endpoints**:
- `/auth/**` → AuthService
- `/users/**` → UserService  
- `/workouts/**` → WorkoutService
- `/meals/**` → MealService
- `/analytics/**` → AnalystService

Xem chi tiết tại [APIService README](./apiservice/README.md)

### 2. 🔐 AuthService

**Port**: 8005 | **Database**: auth_db (3306) | **Role**: Authentication & Authorization

**Core Features**:
- **User Registration**: Account creation with validation
- **JWT Authentication**: Secure login with token generation  
- **Password Management**: Secure password change with token refresh
- **Token Blacklist**: Logout functionality with Redis-based token revocation
- **User Integration**: Automatic UserService profile creation

**Key Endpoints**:
- `POST /auth/signup` - Register new user
- `POST /auth/login` - User authentication
- `POST /auth/change-password` - Password update
- `POST /auth/logout` - Token revocation
- `GET /auth/users/me` - Current user info

Xem chi tiết tại [AuthService README](./authservice/README.md)

### 3. 👤 UserService

**Port**: 8006 | **Database**: user_db (3307) | **Role**: User Profile Management

**Core Features**:
- **Profile Management**: Personal information (name, email, phone, address)
- **Health Data**: Physical metrics (height, weight, birth date, gender)
- **Security**: Read-only email, password changes via AuthService
- **Integration**: Profile creation triggered by AuthService

**Key Endpoints**:
- `GET /users/me` - Current user profile
- `GET /users/{id}` - User by ID
- `PUT /users/{id}` - Update profile  
- `GET /users/email/{email}` - User by email
- `PUT /users/{id}/security` - Security change guidance

Xem chi tiết tại [UserService README](./userservice/README.md)

### 4. 🏃 WorkoutService

**Port**: 8007 | **Database**: workout_db (3309) | **Role**: Workout & Calorie Management

**🔥 Advanced Features**:
- **MET-based Calorie Calculation**: Scientific calorie calculation using Metabolic Equivalent of Task
- **Auto-calculation**: Calories = MET × weight(kg) × duration(hours)
- **7 Workout Types**: CARDIO(7.0), STRENGTH(4.5), HIIT(9.0), YOGA(2.8), PILATES(3.5), CROSSFIT(9.0), FLEXIBILITY(2.5)
- **Exercise Management**: Detailed exercise tracking with sets, reps, duration
- **Schedule Management**: Workout planning with status tracking
- **Calorie Statistics**: Daily/Weekly/Monthly calorie burn analytics

**Key Endpoints**:
- `POST /workouts` - Create workout (auto-calculate calories)
- `POST /workouts/schedules` - Create workout schedule
- `POST /workouts/scheduled-workouts` - Schedule specific workout
- `GET /calories-burned/daily/{userId}` - Daily calorie statistics
- `GET /calories-burned/weekly/{userId}` - Weekly calorie statistics
- `PATCH /workouts/scheduled-workouts/{id}/status/{status}` - Update workout status

Xem chi tiết tại [WorkoutService README](./workoutservice/README.md)

### 5. 🍽️ MealService

**Port**: 8008 | **Database**: meal_db (3310) | **Role**: Nutrition & Meal Management

**🔥 Advanced Features**:
- **Auto-sync Calories**: Meal calories = Sum of food calories (automatic calculation)
- **Meal Types**: BREAKFAST, LUNCH, DINNER, SNACK
- **Meal Status**: SCHEDULED, COMPLETED, CANCELLED
- **Food Management**: Individual food items with calorie tracking
- **Schedule Planning**: Meal scheduling with date ranges
- **Calorie Analytics**: Daily/Weekly/Monthly calorie intake statistics

**Key Endpoints**:
- `POST /meals` - Create meal (auto-calculate calories from foods)
- `POST /meals/schedules` - Create meal schedule
- `POST /meals/scheduled-meals` - Schedule specific meal
- `GET /calories/daily/{userId}` - Daily calorie intake
- `GET /calories/weekly/{userId}` - Weekly calorie intake
- `PATCH /meals/scheduled-meals/{id}/status/{status}` - Update meal status

Xem chi tiết tại [MealService README](./mealservice/README.md)

### 6. 📊 AnalystService

**Port**: 8083 | **Database**: None (Data Consumer) | **Role**: Health Analytics & AI Calculations

**🧠 AI-Powered Features**:
- **Dual Controller Architecture**: 
  - `HealthAnalyticsController`: Basic health metrics (BMI, BMR, TDEE)
  - `ActivityAnalysisController`: AI-based activity analysis
- **Scientific Calculations**: Mifflin-St Jeor equation for BMR
- **Activity Factor Mapping**: 0-7+ workout days → 1.2-1.9 activity factor
- **Smart TDEE**: Workout-based vs Manual calculation strategies
- **Health Recommendations**: Personalized advice based on BMI categories

**Key Endpoints**:
- `GET /analytics/health-analytics/health-metrics/{userId}` - Complete health profile
- `GET /analytics/health-analytics/tdee/{userId}` - TDEE calculation
- `GET /analytics/activity-analysis/user/{userId}` - AI activity analysis  
- `GET /analytics/activity-analysis/user/{userId}/workout-based-tdee` - Smart TDEE
- `POST /analytics/health-analytics/calculate-bmi` - Direct BMI calculation

Xem chi tiết tại [AnalystService README](./analystservice/README.md)

## 🚀 Cài đặt và Chạy

### Yêu cầu hệ thống
- **Java 21** (JDK)
- **Maven 3.9+**
- **Docker & Docker Compose**
- **MySQL 8.0+** 
- **Redis** (cho token blacklist)

### Quick Start với Docker

1. **Khởi tạo databases và Redis:**
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

### Cài đặt và cấu hình Eureka Server (Service Discovery)

Eureka Server giúp các microservice đăng ký và tìm kiếm lẫn nhau:

1. **Tạo Eureka Server project:**
```bash
mkdir discoveryserver
cd discoveryserver
```

2. **Tạo file `pom.xml`:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.3</version>
        <relativePath/>
    </parent>
    <groupId>com.example</groupId>
    <artifactId>discoveryserver</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>discoveryserver</name>
    <description>Eureka Service Discovery Server</description>
    
    <properties>
        <java.version>21</java.version>
        <spring-cloud.version>2023.0.0</spring-cloud.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

3. **Tạo main application class:**
```bash
mkdir -p src/main/java/com/example/discoveryserver
mkdir -p src/main/resources
```

`src/main/java/com/example/discoveryserver/DiscoveryServerApplication.java`:
```java
package com.example.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerApplication.class, args);
    }
}
```

4. **Cấu hình Eureka Server (`src/main/resources/application.properties`):**
```properties
# Server Configuration
server.port=8761

# Eureka Configuration  
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.server.enable-self-preservation=false

# Application Name
spring.application.name=discovery-server
```

### 🏃‍♂️ Khởi chạy các Service

**Thứ tự khởi động quan trọng:**

1. **Khởi chạy Eureka Server:**
```bash
cd discoveryserver
mvn spring-boot:run
```
*Dashboard: http://localhost:8761*

2. **Khởi chạy API Gateway:**
```bash
cd apiservice  
mvn spring-boot:run
```

3. **Khởi chạy các microservices (có thể song song):**
```bash
# Terminal 1
cd authservice
mvn spring-boot:run

# Terminal 2  
cd userservice
mvn spring-boot:run

# Terminal 3
cd workoutservice
mvn spring-boot:run

# Terminal 4
cd mealservice
mvn spring-boot:run

# Terminal 5
cd analystservice
mvn spring-boot:run
```

### ✅ Health Check

Kiểm tra các service đã startup thành công:

```bash
# Eureka Dashboard
curl http://localhost:8761

# API Gateway Health
curl http://localhost:8080/actuator/health

# Individual Services Health  
curl http://localhost:8005/actuator/health  # AuthService
curl http://localhost:8006/actuator/health  # UserService
curl http://localhost:8007/actuator/health  # WorkoutService
curl http://localhost:8008/actuator/health  # MealService
curl http://localhost:8009/actuator/health  # AnalystService
```

## 🧪 Testing với Postman

### Environment Setup
Tạo Postman Environment với variables:
```json
{
  "api_gateway": "http://localhost:8080",
  "jwt_token": "",
  "user_id": "",
  "user_email": "test@example.com"
}
```

### Complete Test Flow

1. **User Registration:**
```
POST {{api_gateway}}/auth/signup
{
  "fullName": "John Doe",
  "email": "john@example.com", 
  "password": "password123"
}
```

2. **User Login:**
```
POST {{api_gateway}}/auth/login
{
  "email": "john@example.com",
  "password": "password123"
}
```
*Save JWT token to environment*

3. **Get User Profile:**
```
GET {{api_gateway}}/users/me
Authorization: Bearer {{jwt_token}}
```

4. **Create Workout with Auto-Calorie Calculation:**
```
POST {{api_gateway}}/workouts
Authorization: Bearer {{jwt_token}}
{
  "name": "HIIT Training",
  "description": "High intensity interval training",
  "type": "HIIT",
  "durationMinutes": 30,
  "caloriesBurned": 405,
  "exercises": [...]
}
```

5. **Create Meal with Auto-Calorie Sync:**
```
POST {{api_gateway}}/meals
Authorization: Bearer {{jwt_token}}
{
  "name": "Protein Breakfast",
  "type": "BREAKFAST",
  "foods": [
    {
      "name": "Eggs",
      "calories": 150
    },
    {
      "name": "Oatmeal", 
      "calories": 200
    }
  ]
}
```

6. **Get Health Analytics:**
```
GET {{api_gateway}}/analytics/health-analytics/health-metrics/{{user_id}}
Authorization: Bearer {{jwt_token}}
```

7. **Get Calorie Statistics:**
```
GET {{api_gateway}}/workouts/calories-burned/daily/{{user_id}}?date=2024-01-15
Authorization: Bearer {{jwt_token}}

GET {{api_gateway}}/meals/calories/daily/{{user_id}}?date=2024-01-15  
Authorization: Bearer {{jwt_token}}
```

Xem chi tiết test cases trong README của từng service.

## 📈 Performance & Monitoring

### Monitoring Endpoints
- **Eureka Dashboard**: http://localhost:8761
- **Actuator Health**: `/actuator/health` trên mỗi service
- **Metrics**: `/actuator/metrics` 
- **Prometheus**: `/actuator/prometheus` (if enabled)
