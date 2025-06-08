# Docker Microservices Setup Guide

## Tổng quan
Hệ thống bao gồm 7 microservices được containerized và cấu hình để giao tiếp với nhau:

### Services Architecture
```
API Gateway (8080) 
    ↓
├── Discovery Server (8761)
├── Auth Service (8005) → auth-db (3307)
├── User Service (8006) → user-db (3308)
├── Workout Service (8007) → workout-db (3309)
├── Meal Service (8008) → meal-db (3310)
└── Analyst Service (8009) → analyst-db (3311)
    ↓
Redis (6379)
```

## Cách chạy hệ thống

### 1. Build và khởi động tất cả services
```bash
docker-compose up --build -d
```

### 2. Kiểm tra trạng thái các services
```bash
docker-compose ps
```

### 3. Xem logs của specific service
```bash
# Xem logs của tất cả services
docker-compose logs -f

# Xem logs của một service cụ thể
docker-compose logs -f auth-service
docker-compose logs -f user-service
docker-compose logs -f analyst-service
```

### 4. Test kết nối giữa các services
```bash
# Trên Linux/macOS
chmod +x test-services-connectivity.sh
./test-services-connectivity.sh

# Trên Windows (Git Bash)
bash test-services-connectivity.sh
```

## Service Endpoints

### Discovery Server
- **URL:** http://localhost:8761
- **Health Check:** http://localhost:8761/actuator/health

### API Gateway
- **URL:** http://localhost:8080
- **Health Check:** http://localhost:8080/actuator/health

### Auth Service
- **Direct:** http://localhost:8005
- **Via Gateway:** http://localhost:8080/auth/**
- **Health Check:** http://localhost:8005/actuator/health

### User Service
- **Direct:** http://localhost:8006
- **Via Gateway:** http://localhost:8080/users/**
- **Health Check:** http://localhost:8006/actuator/health

### Workout Service
- **Direct:** http://localhost:8007
- **Via Gateway:** http://localhost:8080/workouts/**
- **Health Check:** http://localhost:8007/actuator/health

### Meal Service
- **Direct:** http://localhost:8008
- **Via Gateway:** http://localhost:8080/meals/**
- **Health Check:** http://localhost:8008/actuator/health

### Analyst Service
- **Direct:** http://localhost:8009
- **Via Gateway:** http://localhost:8080/analytics/**
- **Health Check:** http://localhost:8009/actuator/health

## Database Connections

### MySQL Databases
- **Auth DB:** localhost:3307
- **User DB:** localhost:3308
- **Workout DB:** localhost:3309
- **Meal DB:** localhost:3310
- **Analyst DB:** localhost:3311

### Redis
- **URL:** localhost:6379

## Network Configuration

Tất cả services chạy trong cùng một Docker network: `health-app-network`

### Service Discovery
- Services tự động đăng ký với **Eureka Discovery Server**
- Load balancing thông qua **Spring Cloud Gateway**

### Inter-Service Communication
Services giao tiếp với nhau thông qua:
1. **Service Discovery** (Eureka)
2. **Container Names** trong Docker network
3. **API Gateway** cho external requests

## Cấu hình Environment Variables

### Docker Profiles
Mỗi service có file cấu hình riêng cho Docker:
- `application-docker.properties`
- Được kích hoạt bằng `SPRING_PROFILES_ACTIVE=docker`

### Key Environment Variables
- **Database URLs:** Sử dụng container names (vd: `auth-db:3306`)
- **Service URLs:** Sử dụng container names (vd: `http://user-service:8006`)
- **Eureka URL:** `http://discovery-server:8761/eureka`
- **Redis URL:** `redis:6379`

## Troubleshooting

### 1. Service không start được
```bash
# Kiểm tra logs
docker-compose logs [service-name]

# Restart specific service
docker-compose restart [service-name]
```

### 2. Database connection issues
```bash
# Kiểm tra database container
docker-compose logs auth-db
docker-compose logs user-db

# Restart database
docker-compose restart auth-db
```

### 3. Services không register với Eureka
```bash
# Kiểm tra Discovery Server
docker-compose logs discovery-server

# Kiểm tra network connectivity
docker network inspect health-app-network
```

### 4. Clean up và restart
```bash
# Stop tất cả services
docker-compose down

# Remove volumes (sẽ xóa data)
docker-compose down -v

# Remove images
docker-compose down --rmi all

# Rebuild từ đầu
docker-compose up --build -d
```

## Health Checks

Tất cả services đều có health check endpoints:
- Endpoint: `/actuator/health`
- Tự động kiểm tra database connectivity
- Load balancer sẽ remove unhealthy instances

## Best Practices

1. **Luôn kiểm tra logs** khi có vấn đề
2. **Wait for dependencies:** Services có dependency hierarchy
3. **Use service names** thay vì IP addresses
4. **Monitor Eureka dashboard** tại http://localhost:8761
5. **Test through Gateway** thay vì direct service calls

## Security

- **JWT Secret:** Shared across all services
- **Database passwords:** Configured in environment variables
- **Redis:** No authentication (internal network only)
- **Service-to-service:** Through service discovery 