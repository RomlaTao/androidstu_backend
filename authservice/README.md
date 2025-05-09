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
  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```
  Response:
  ```json
  {
    "token": "string",
    "expiresIn": "number"
  }
  ```

- **POST** `/auth/change-password` - Đổi mật khẩu (yêu cầu JWT)
  ```json
  {
    "currentPassword": "string",
    "newPassword": "string"
  }
  ```
  Response:
  ```json
  {
    "message": "Password changed successfully",
    "token": "string", 
    "expiresIn": "number"
  }
  ```

- **POST** `/auth/logout` - Đăng xuất (yêu cầu JWT)
  Response:
  ```json
  {
    "message": "Logged out successfully"
  }
  ```

### Users
- **GET** `/users/me` - Lấy thông tin người dùng hiện tại (yêu cầu JWT)
- **GET** `/users/` - Lấy danh sách tất cả người dùng (yêu cầu JWT)

## Cài đặt và Chạy

### Yêu cầu
- Java Development Kit (JDK) 21
- Maven 3.9+
- MySQL 8.0+
- Redis (tùy chọn cho JWT blacklist)

### Cài đặt MySQL bằng Docker

1. Tạo container MySQL
```bash
docker run --name mysql-auth -e MYSQL_ROOT_PASSWORD=yourpassword -e MYSQL_DATABASE=auth_db -p 3306:3306 -d mysql:8.0
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
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/auth_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT Configuration
security.jwt.secret-key=your_secret_key_base64_encoded
security.jwt.expiration-time=86400000  # 24 hours in milliseconds

# Redis Configuration (cho JWT blacklist)
spring.redis.host=localhost
spring.redis.port=6379
```

3. Build project
```bash
mvn clean install
```

4. Chạy ứng dụng
```bash
mvn spring-boot:run
```

Ứng dụng sẽ chạy mặc định trên port 8080.

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