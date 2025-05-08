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

### Users
- **GET** `/users/me` - Lấy thông tin người dùng hiện tại (yêu cầu JWT)
- **GET** `/users/` - Lấy danh sách tất cả người dùng (yêu cầu JWT)

## Cài đặt và Chạy

### Yêu cầu
- Java Development Kit (JDK) 21
- Maven 3.9+
- MySQL 8.0+

### Các bước cài đặt

1. Clone repository
```bash
git clone <repository-url>
cd authservice
```

2. Cấu hình database trong `src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Configuration
security.jwt.secret-key=your_secret_key_base64_encoded
security.jwt.expiration-time=86400000  # 24 hours in milliseconds
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

## Bảo mật
- Xác thực dựa trên JWT (JSON Web Token)
- Mật khẩu được mã hóa bằng BCrypt
- Spring Security với các cấu hình bảo mật mặc định
- Stateless authentication
- CORS được cấu hình sẵn

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

## Phát triển
- Sử dụng các branch riêng cho mỗi tính năng mới
- Tuân thủ code style của project
- Viết unit test cho các chức năng mới
- Sử dụng meaningful commit messages

## License
[MIT License](LICENSE) 