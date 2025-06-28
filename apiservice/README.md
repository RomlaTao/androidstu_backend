# API Gateway Service - High Availability

## Chức năng chính

**API Gateway** là điểm vào duy nhất của hệ thống microservices, được triển khai với **High Availability** (2 instances) và **HAProxy Load Balancer** để đảm bảo zero-downtime.

### 🚪 Tính Năng Gateway
- **High Availability**: 2 instances (api-service-1:8080, api-service-2:8081)
- **Load Balancing**: HAProxy phân phối traffic round-robin
- **Định tuyến (Routing)**: Định tuyến request đến đúng microservice
- **Xác thực (Authentication)**: Xác thực JWT token tập trung
- **Danh sách đen Token (Token Blacklist)**: Kiểm tra token bị vô hiệu hóa qua Redis
- **Ngắt mạch (Circuit Breaker)**: Ngăn chặn lỗi lan truyền
- **Giới hạn tốc độ (Rate Limiting)**: Giới hạn số request per client
- **CORS**: Cấu hình Cross-Origin Resource Sharing
- **Health Checks**: Monitoring cho HAProxy failover
- **Ghi log (Logging)**: Ghi log tất cả request/response

### 🛣️ Định Tuyến Dịch Vụ
| Đường dẫn | Dịch vụ | Yêu cầu xác thực |
|------|---------|---------------|
| `/auth/**` | AuthService | ❌ |
| `/users/**` | UserService | ✅ |
| `/workouts/**` | WorkoutService | ✅ |
| `/meals/**` | MealService | ✅ |
| `/analytics/**` | AnalystService | ✅ |

### 🏗️ Architecture với HAProxy

```
┌─────────────────────┐
│      HAProxy        │
│   (Load Balancer)   │
│  https://localhost  │
│  Stats: :8404       │
└──────────┬──────────┘
           │ Round Robin
     ┌─────┴─────┐
     │           │
┌────▼────┐ ┌────▼────┐
│API GW 1 │ │API GW 2 │
│  :8080  │ │  :8081  │
│Instance │ │Instance │
└─────────┘ └─────────┘
     │           │
     └─────┬─────┘
           │
    Backend Services
```

## Cấu hình

### High Availability Setup
```yaml
# docker-compose.yml
api-service-1:
  container_name: api-service-1
  ports:
    - "8080:8080"
  environment:
    - EUREKA_INSTANCE_HOSTNAME=api-service-1

api-service-2:
  container_name: api-service-2
  ports:
    - "8081:8080"
  environment:
    - EUREKA_INSTANCE_HOSTNAME=api-service-2

haproxy:
  ports:
    - "80:80"
    - "443:443"
    - "8404:8404"
  depends_on:
    - api-service-1
    - api-service-2
```

### Cấu Hình Cơ Bản
```properties
# Máy chủ
server.port=8080  # Internal port, HAProxy routes externally

# Khám phá dịch vụ Eureka
eureka.client.service-url.defaultZone=http://discovery-server:8761/eureka/
eureka.instance.hostname=${EUREKA_INSTANCE_HOSTNAME:api-service}

# Bảo mật JWT
security.jwt.secret-key=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
security.jwt.expiration-time=3600000

# Redis (Danh sách đen Token)
spring.redis.host=redis
spring.redis.port=6379

# Health Checks cho HAProxy
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always

# Ngắt mạch
resilience4j.circuitbreaker.instances.default.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.default.wait-duration-in-open-state=5000ms

# Giới hạn tốc độ
resilience4j.ratelimiter.instances.default.limit-for-period=100
resilience4j.ratelimiter.instances.default.limit-refresh-period=1s
```

### Hạ Tầng Cần Thiết
- **HAProxy Load Balancer**: SSL termination và traffic distribution
- **Eureka Server** (port 8761): Khám phá dịch vụ
- **Redis** (port 6379): Lưu trữ danh sách đen token
- **Backend Services**: AuthService, UserService, WorkoutService, MealService, AnalystService

### Ngăn Xếp Công Nghệ
- Spring Cloud Gateway
- Spring Security + JWT
- Redis (Danh sách đen)
- Resilience4j (Ngắt mạch + Giới hạn tốc độ)
- Caffeine Cache
- Spring Boot Actuator (Health Checks)

## 🚦 Testing & Monitoring

### Load Balancer Testing
```bash
# Test through HAProxy (recommended)
curl -k https://localhost/actuator/health

# Test individual instances
curl http://localhost:8080/actuator/health  # Instance 1
curl http://localhost:8081/actuator/health  # Instance 2

# Test load balancing distribution
for i in {1..10}; do
  curl -k -H "X-Request-ID: $i" https://localhost/actuator/health
done
```

### Failover Testing
```bash
# Stop instance 1 and test automatic failover
docker-compose stop api-service-1
curl -k https://localhost/actuator/health  # Should still work via instance 2

# Restart and verify load balancing resumes
docker-compose start api-service-1
# Check HAProxy stats at https://localhost:8404
```

### Performance Testing
```bash
# Test SSL termination performance
ab -n 1000 -c 10 -k https://localhost/actuator/health

# Test concurrent load handling
ab -n 10000 -c 100 https://localhost/auth/health
```

### HAProxy Monitoring
- **Stats Dashboard**: https://localhost:8404 (admin/12345)
- **Real-time Metrics**: Server status, response times, error rates
- **Health Status**: Backend server availability
- **Traffic Distribution**: Request count per instance

### Kiểm Thử Giới Hạn Tốc Độ
**Thiết lập**: Gửi 100+ request nhanh chóng đến bất kỳ endpoint nào
**Kết quả mong đợi**: 429 Too Many Requests sau 100 request

### Kiểm Thử Ngắt Mạch
**Thiết lập**: Dừng một backend service, sau đó gửi request
**Kết quả mong đợi**: Circuit breaker kích hoạt và trả về lỗi có kiểm soát

### Script Xác Thực
```javascript
// Kiểm tra High Availability
pm.test("Load balancer is working", function () {
    pm.response.to.not.have.status(503);
});

// Kiểm tra SSL termination
pm.test("HTTPS is working", function () {
    pm.expect(pm.request.url.protocol).to.equal('https:');
});

// Kiểm tra xác thực
pm.test("Request đã được xác thực", function () {
    pm.response.to.not.have.status(401);
});

// Kiểm tra giới hạn tốc độ
pm.test("Không vượt quá giới hạn tốc độ", function () {
    pm.response.to.not.have.status(429);
});

// Kiểm tra hiệu suất
pm.test("Thời gian phản hồi < 2s", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});

// Kiểm tra ngắt mạch
pm.test("Ngắt mạch hoạt động", function () {
    if (pm.response.code === 503) {
        pm.test("Dịch vụ không khả dụng được xử lý một cách có kiểm soát");
    }
});
```

## High Availability Features

### Auto-Failover
- HAProxy tự động phát hiện instance down qua health checks
- Traffic được chuyển hướng ngay lập tức sang instance còn lại
- Không có downtime cho end users

### Load Distribution
- **Algorithm**: Round Robin (có thể thay đổi thành leastconn, source)
- **Sticky Sessions**: Không cần thiết vì stateless JWT
- **Health Checks**: GET `/actuator/health` mỗi 10s

### Scaling
```bash
# Scale up API Gateway instances
docker-compose up -d --scale api-service=3

# Update HAProxy configuration to include new instances
# Restart HAProxy to apply new config
docker-compose restart haproxy
```

## Luồng Gateway với Load Balancer
1. **Client** → HAProxy (HTTPS :443)
2. **HAProxy** → SSL Termination
3. **HAProxy** → Load Balance → API Gateway Instance
4. **Gateway** → Xác thực JWT
5. **Gateway** → Kiểm tra danh sách đen Token (Redis)
6. **Gateway** → Kiểm tra giới hạn tốc độ
7. **Gateway** → Kiểm tra ngắt mạch
8. **Gateway** → Dịch vụ Backend (thông qua Eureka)
9. **Backend** → Phản hồi → **Gateway** → **HAProxy** → **Client**

## Production Considerations

### SSL Certificate Management
```bash
# Generate production certificates
./haproxy/generate-ssl.sh

# Update certificate without downtime
# Place new cert in ./ssl/haproxy.pem
docker-compose restart haproxy  # Graceful restart
```

### Monitoring Integration
- **Health Endpoint**: `/actuator/health` for HAProxy
- **Metrics**: `/actuator/metrics` for monitoring systems
- **Prometheus**: `/actuator/prometheus` if enabled
- **Custom Health Indicators**: Database, Redis connectivity

### Security Headers (via HAProxy)
- **HSTS**: Strict-Transport-Security
- **XSS Protection**: X-XSS-Protection
- **Frame Options**: X-Frame-Options DENY
- **Content Type**: X-Content-Type-Options nosniff 