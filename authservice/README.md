# Authentication Service

## Giới thiệu
Authentication Service là một microservice được xây dựng bằng Spring Boot, cung cấp các chức năng xác thực và quản lý người dùng. Service này sử dụng JWT (JSON Web Token) để xác thực người dùng và Spring Security để bảo mật.

## Công nghệ sử dụng
- Java 21
- Spring Boot 3.2.3
- Spring Security
- JWT (JJWT 0.11.5)
- Spring Data JPA
- MySQL Database
- Redis (cho JWT blacklist)
- Maven

## Cấu trúc Project
```
authservice/
├── src/main/java/com/example/authservice/
│   ├── configs/            # Cấu hình Spring Security và JWT
│   ├── controllers/        # REST Controllers
│   ├── dtos/              # Data Transfer Objects
│   ├── entities/          # JPA Entities
│   ├── exceptions/        # Exception Handlers
│   ├── repositories/      # Spring Data Repositories
│   ├── responses/         # Response Models
│   ├── services/         # Business Logic Services
│   └── AuthserviceApplication.java
```

## API Endpoints

### Authentication
- **POST** `/auth/signup` - Đăng ký người dùng mới
  - API Gateway URL: `http://localhost:8080/auth/signup`
  ```json
  {
    "fullName": "string",
    "email": "string",
    "password": "string"
  }
  ```
  Response:
  ```json
  {
    "id": "number",
    "fullName": "string",
    "email": "string",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
  ```

- **POST** `/auth/login` - Đăng nhập
  - API Gateway URL: `http://localhost:8080/auth/login`
  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```
  Response:
  ```json
  {
    "accessToken": "string",
    "tokenType": "Bearer",
    "expiresIn": "number"
  }
  ```

- **POST** `/auth/change-password` - Đổi mật khẩu (yêu cầu JWT)
  - API Gateway URL: `http://localhost:8080/auth/change-password`
  ```json
  {
    "email": "nguyenvana@example.com",
    "currentPassword": "NewPassword456",
    "newPassword": "Password123"
  }
  ```
  Response:
  ```json
  {
    "message": "Password changed successfully",
    "accessToken": "string", 
    "expiresIn": "number"
  }
  ```

- **POST** `/auth/logout` - Đăng xuất (yêu cầu JWT)
  - API Gateway URL: `http://localhost:8080/auth/logout`
  Response:
  ```json
  {
    "message": "Logged out successfully"
  }
  ```

### Users
- **GET** `/auth/users/me` - Lấy thông tin người dùng hiện tại (yêu cầu JWT)
- **GET** `/auth/users/` - Lấy danh sách tất cả người dùng (yêu cầu JWT)

## Cài đặt và Chạy

### Yêu cầu
- Java Development Kit (JDK) 21
- Maven 3.9+
- MySQL 8.0+
- Redis (tùy chọn cho JWT blacklist)

### Cài đặt MySQL bằng Docker

1. Tạo container MySQL
```bash
docker run --name auth-db -e MYSQL_ROOT_PASSWORD=yourpassword -e MYSQL_DATABASE=auth_db -p 3306:3306 -d mysql:8.0
```

2. Kiểm tra container đã chạy
```bash
docker ps
```

3. Kết nối đến MySQL
```bash
docker exec -it mysql-auth mysql -u root -p
```

### Cài đặt Redis bằng Docker (cho JWT blacklist)

1. Tạo container Redis
```bash
docker run --name redis-auth -p 6379:6379 -d redis
```

2. Kiểm tra container đã chạy
```bash
docker ps
```

### Các bước cài đặt

1. Clone repository
```bash
git clone <repository-url>
cd authservice
```

2. Cấu hình database trong `src/main/resources/application.properties`
```properties
# Server Configuration
server.port=8005

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3307/auth_db
spring.datasource.username=root
spring.datasource.password=secret

# Hibernate properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT Configuration
security.jwt.secret-key=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
# 1h in millisecond
security.jwt.expiration-time=3600000

# Redis Configuration (cho JWT blacklist)
spring.redis.host=localhost
spring.redis.port=6379

# Eureka Configuration
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.prefer-ip-address=true
```

3. Build project
```bash
mvn clean install
```

4. Chạy ứng dụng
```bash
mvn spring-boot:run
```

Ứng dụng sẽ chạy trên port 8005.

## Thử nghiệm với Postman

### Collection Setup

1. Tạo một collection mới trong Postman với tên "Auth Service API"
2. Đặt biến môi trường:
   - `base_url`: http://localhost:8080
   - `token`: <JWT token từ quá trình đăng nhập>

### Test Cases

1. **Đăng Ký Người Dùng**
   - Method: POST
   - URL: {{base_url}}/auth/signup
   - Headers: Content-Type: application/json
   - Body:
     ```json
     {
       "fullName": "Test User",
       "email": "test@example.com",
       "password": "password123"
     }
     ```
   - Expected: 201 Created với thông tin người dùng

2. **Đăng Nhập**
   - Method: POST
   - URL: {{base_url}}/auth/login
   - Headers: Content-Type: application/json
   - Body:
     ```json
     {
       "email": "test@example.com",
       "password": "password123"
     }
     ```
   - Script (để lưu token):
     ```javascript
     pm.environment.set("token", pm.response.json().accessToken);
     ```
   - Expected: 200 OK với JWT token

3. **Đổi Mật Khẩu**
   - Method: POST
   - URL: {{base_url}}/auth/change-password
   - Headers: 
     - Content-Type: application/json
     - Authorization: Bearer {{token}}
   - Body:
     ```json
     {
       "currentPassword": "password123",
       "newPassword": "newpassword123"
     }
     ```
   - Expected: 200 OK với thông báo thành công và token mới

4. **Đăng Xuất**
   - Method: POST
   - URL: {{base_url}}/auth/logout
   - Headers: Authorization: Bearer {{token}}
   - Expected: 200 OK với thông báo thành công

## Tính năng
- Đăng ký và đăng nhập người dùng
- Xác thực thông qua JWT token
- Đổi mật khẩu với cấp token mới
- Đăng xuất với JWT blacklist
- Tích hợp với UserService để quản lý thông tin người dùng

## Bảo mật
- Xác thực dựa trên JWT (JSON Web Token)
- Mật khẩu được mã hóa bằng BCrypt
- Spring Security với các cấu hình bảo mật mặc định
- Stateless authentication
- JWT blacklist để quản lý token đã hết hạn hoặc đăng xuất
- CORS được cấu hình sẵn

## Quy trình đăng ký và đăng nhập
1. Người dùng đăng ký tài khoản thông qua `/auth/signup`
2. AuthService lưu thông tin người dùng vào database
3. AuthService gọi UserService API để tạo profile người dùng
4. Khi đăng nhập, AuthService kiểm tra thông tin và cấp JWT token
5. JWT token được sử dụng để xác thực các request tiếp theo

## Quy trình đổi mật khẩu và đăng xuất
1. Khi đổi mật khẩu, AuthService kiểm tra mật khẩu hiện tại
2. Sau khi đổi mật khẩu thành công, một token mới được cấp
3. Khi đăng xuất, token hiện tại được thêm vào blacklist
4. Token trong blacklist không thể sử dụng để xác thực

## Cấu trúc Database
### Bảng Users
- id (Integer, Primary Key)
- full_name (String)
- email (String, Unique)
- password (String, Encrypted)
- created_at (Timestamp)
- updated_at (Timestamp)

## Môi trường
Các biến môi trường cần thiết:
- `MYSQL_HOST`: Host của MySQL database
- `MYSQL_PORT`: Port của MySQL database
- `MYSQL_DATABASE`: Tên database
- `MYSQL_USER`: Username MySQL
- `MYSQL_PASSWORD`: Password MySQL
- `JWT_SECRET_KEY`: Khóa bí mật cho JWT (Base64 encoded)
- `JWT_EXPIRATION`: Thời gian hết hạn của JWT (milliseconds)
- `REDIS_HOST`: Host của Redis (cho JWT blacklist)
- `REDIS_PORT`: Port của Redis (cho JWT blacklist)
- `USER_SERVICE_URL`: URL của UserService

## Phát triển
- Sử dụng các branch riêng cho mỗi tính năng mới
- Tuân thủ code style của project
- Viết unit test cho các chức năng mới
- Sử dụng meaningful commit messages

## Giải pháp thay thế cho Redis
Nếu không muốn sử dụng Redis cho JWT blacklist, có thể dùng giải pháp InMemoryTokenBlacklist:

1. Tạo class InMemoryTokenBlacklist
```java
@Service
public class InMemoryTokenBlacklist {
    private final Set<String> blacklist = new ConcurrentHashMap<String, Boolean>().newKeySet();
    
    public void addToBlacklist(String token) {
        blacklist.add(token);
    }
    
    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
```

2. Cập nhật JwtAuthenticationFilter để sử dụng InMemoryTokenBlacklist

## License
[MIT License](LICENSE) 