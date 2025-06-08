# Dịch Vụ API Gateway

## Chức năng chính

**API Gateway** là điểm vào duy nhất của hệ thống microservices, đóng vai trò trung gian giữa client và các service backend.

### 🚪 Tính Năng Gateway
- **Định tuyến (Routing)**: Định tuyến request đến đúng microservice
- **Xác thực (Authentication)**: Xác thực JWT token tập trung
- **Danh sách đen Token (Token Blacklist)**: Kiểm tra token bị vô hiệu hóa qua Redis
- **Ngắt mạch (Circuit Breaker)**: Ngăn chặn lỗi lan truyền
- **Giới hạn tốc độ (Rate Limiting)**: Giới hạn số request per client
- **CORS**: Cấu hình Cross-Origin Resource Sharing
- **Ghi log (Logging)**: Ghi log tất cả request/response

### 🛣️ Định Tuyến Dịch Vụ
| Đường dẫn | Dịch vụ | Yêu cầu xác thực |
|------|---------|---------------|
| `/auth/**` | AuthService | ❌ |
| `/users/**` | UserService | ✅ |
| `/workouts/**` | WorkoutService | ✅ |
| `/meals/**` | MealService | ✅ |
| `/analytics/**` | AnalystService | ✅ |

## Cấu hình

### Cấu Hình Cơ Bản
```properties
# Máy chủ
server.port=8080

# Khám phá dịch vụ Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Bảo mật JWT
security.jwt.secret-key=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
security.jwt.expiration-time=3600000

# Redis (Danh sách đen Token)
spring.redis.host=localhost
spring.redis.port=6379

# Ngắt mạch
resilience4j.circuitbreaker.instances.default.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.default.wait-duration-in-open-state=5000ms

# Giới hạn tốc độ
resilience4j.ratelimiter.instances.default.limit-for-period=100
resilience4j.ratelimiter.instances.default.limit-refresh-period=1s
```

### Hạ Tầng Cần Thiết
- **Eureka Server** (port 8761): Khám phá dịch vụ
- **Redis** (port 6379): Lưu trữ danh sách đen token
- **Dịch vụ Backend**: AuthService, UserService, WorkoutService, MealService, AnalystService

### Ngăn Xếp Công Nghệ
- Spring Cloud Gateway
- Spring Security + JWT
- Redis (Danh sách đen)
- Resilience4j (Ngắt mạch + Giới hạn tốc độ)
- Caffeine Cache

## 🚦 Kiểm Thử Hiệu Suất

### Kiểm Thử Giới Hạn Tốc Độ
**Thiết lập**: Gửi 100+ request nhanh chóng đến bất kỳ endpoint nào
**Kết quả mong đợi**: 429 Too Many Requests sau 100 request

### Kiểm Thử Ngắt Mạch
**Thiết lập**: Dừng một backend service, sau đó gửi request
**Kết quả mong đợi**: Circuit breaker kích hoạt và trả về lỗi có kiểm soát

### Script Xác Thực
```javascript
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

## Luồng Gateway
1. **Client** → API Gateway (8080)
2. **Gateway** → Xác thực JWT
3. **Gateway** → Kiểm tra danh sách đen Token (Redis)
4. **Gateway** → Kiểm tra giới hạn tốc độ
5. **Gateway** → Kiểm tra ngắt mạch
6. **Gateway** → Dịch vụ Backend (thông qua Eureka)
7. **Backend** → Phản hồi → **Gateway** → **Client** 