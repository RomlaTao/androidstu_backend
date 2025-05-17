# User Service

## Giới thiệu
User Service là một microservice được xây dựng bằng Spring Boot, cung cấp các chức năng quản lý thông tin người dùng và dữ liệu sức khỏe. Service này tương tác với AuthService để xác thực người dùng thông qua JWT.

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
userservice/
├── src/main/java/com/example/userservice/
│   ├── configs/            # Cấu hình Spring Security và JWT
│   ├── controllers/        # REST Controllers
│   ├── dtos/              # Data Transfer Objects
│   ├── entities/          # JPA Entities
│   ├── exceptions/        # Exception Handlers
│   ├── repositories/      # Spring Data Repositories
│   ├── services/         # Business Logic Services
│   └── UserserviceApplication.java
```

## API Endpoints

### Users
- **GET** `/users` - Lấy danh sách tất cả người dùng (yêu cầu JWT)
  - API Gateway URL: `http://localhost:8080/users`
  Response:
  ```json
  [
    {
      "id": "number",
      "fullName": "string",
      "email": "string",
      "phoneNumber": "string",
      "address": "string",
      "birthDate": "date",
      "gender": "string",
      "height": "number",
      "weight": "number",
      "createdAt": "datetime",
      "updatedAt": "datetime"
    }
  ]
  ```

- **GET** `/users/{id}` - Lấy thông tin người dùng theo ID (yêu cầu JWT)
  - API Gateway URL: `http://localhost:8080/users/{id}`
  Response:
  ```json
  {
    "id": "number",
    "fullName": "string",
    "email": "string",
    "phoneNumber": "string",
    "address": "string",
    "birthDate": "date",
    "gender": "string",
    "height": "number",
    "weight": "number",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
  ```

- **GET** `/users/me` - Lấy thông tin người dùng hiện tại từ token JWT (yêu cầu JWT)
  - API Gateway URL: `http://localhost:8080/users/me`
  Response: UserDto object (định dạng như trên)

- **GET** `/users/email/{email}` - Lấy thông tin người dùng theo email (yêu cầu JWT)
  - API Gateway URL: `http://localhost:8080/users/email/{email}`
  Response: UserDto object (định dạng như trên)

- **POST** `/users` - Tạo người dùng mới (chủ yếu sử dụng bởi AuthService)
  - API Gateway URL: `http://localhost:8080/users`
  Request Body:
  ```json
  {
    "fullName": "string",
    "email": "string",
    "phoneNumber": "string",
    "address": "string",
    "birthDate": "date",
    "gender": "string",
    "height": "number",
    "weight": "number"
  }
  ```
  Response: UserDto object đã tạo (định dạng như trên)

- **PUT** `/users/{id}` - Cập nhật thông tin người dùng (yêu cầu JWT)
  - API Gateway URL: `http://localhost:8080/users/{id}`
  Request Body:
  ```json
  {
    "fullName": "string",
    "phoneNumber": "string",
    "address": "string",
    "birthDate": "date",
    "gender": "string",
    "height": "number",
    "weight": "number"
  }
  ```
  Lưu ý: Không thể cập nhật email và password qua endpoint này
  Response: UserDto object đã cập nhật (định dạng như trên)

- **PUT** `/users/{id}/security` - Hướng dẫn về thay đổi thông tin bảo mật (yêu cầu JWT)
  - API Gateway URL: `http://localhost:8080/users/{id}/security`
  Response:
  ```json
  {
    "message": "Để thay đổi email và mật khẩu, vui lòng sử dụng AuthService",
    "changePasswordEndpoint": "/auth/change-password"
  }
  ```

- **DELETE** `/users/{id}` - Xóa người dùng (yêu cầu JWT)
  - API Gateway URL: `http://localhost:8080/users/{id}`
  Response: HTTP 204 No Content

## Cài đặt và Chạy

### Yêu cầu
- Java Development Kit (JDK) 21
- Maven 3.9+
- MySQL 8.0+

### Cài đặt MySQL bằng Docker

1. Tạo container MySQL
```bash
docker run --name mysql-user -e MYSQL_ROOT_PASSWORD=yourpassword -e MYSQL_DATABASE=user_db -p 3307:3306 -d mysql:8.0
```

2. Kiểm tra container đã chạy
```bash
docker ps
```

3. Kết nối đến MySQL
```bash
docker exec -it mysql-user mysql -u root -p
```

### Các bước cài đặt

1. Clone repository
```bash
git clone <repository-url>
cd userservice
```

2. Cấu hình database trong `src/main/resources/application.properties`
```properties
# Server Configuration
server.port=8006

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3307/user_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

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

Ứng dụng sẽ chạy mặc định trên port 8006.

## Kiểm thử API với Postman

### Collection Setup

1. Tạo một collection mới trong Postman với tên "User Service API"
2. Đặt biến môi trường:
   - `base_url`: http://localhost:8080
   - `token`: <JWT token từ quá trình đăng nhập>

### Test Cases

1. **Đăng nhập để lấy token**
   - Method: POST
   - URL: {{base_url}}/auth/login
   - Headers: Content-Type: application/json
   - Body:
     ```json
     {
       "email": "user@example.com",
       "password": "password123"
     }
     ```
   - Script (để lưu token):
     ```javascript
     pm.environment.set("token", pm.response.json().accessToken);
     ```

2. **Lấy Thông Tin Người Dùng Hiện Tại**
   - Method: GET
   - URL: {{base_url}}/users/me
   - Headers: Authorization: Bearer {{token}}
   - Expected: 200 OK với thông tin người dùng

3. **Cập Nhật Thông Tin Người Dùng**
   - Method: PUT
   - URL: {{base_url}}/users/1
   - Headers: 
     - Content-Type: application/json
     - Authorization: Bearer {{token}}
   - Body:
     ```json
     {
       "fullName": "Updated Name",
       "height": 175,
       "weight": 70
     }
     ```
   - Expected: 200 OK với thông tin người dùng đã cập nhật

## Tính năng
- Quản lý thông tin cá nhân người dùng
- Quản lý dữ liệu sức khỏe (chiều cao, cân nặng)
- Tích hợp với AuthService thông qua JWT
- Cung cấp API để quản lý profile người dùng

## Bảo mật
- Xác thực dựa trên JWT (JSON Web Token) từ AuthService
- Spring Security với các cấu hình bảo mật mặc định
- Stateless authentication
- CORS được cấu hình sẵn

## Quy trình hoạt động
1. Khi người dùng đăng ký qua AuthService, AuthService sẽ gọi API tạo người dùng
2. UserService lưu thông tin người dùng vào database riêng
3. Khi cần truy cập thông tin người dùng, client gửi request với JWT token
4. UserService xác thực JWT token và thực hiện các thao tác với database
5. Thông tin email và password không thể sửa đổi qua UserService

## Cấu trúc Database
### Bảng Users
- id (Integer, Primary Key)
- full_name (String)
- email (String, Unique)
- phone_number (String)
- address (String)
- birth_date (Date)
- gender (String)
- height (Float)
- weight (Float)
- created_at (Timestamp)
- updated_at (Timestamp)

## Môi trường
Các biến môi trường cần thiết:
- `SERVER_PORT`: Port của UserService
- `MYSQL_HOST`: Host của MySQL database
- `MYSQL_PORT`: Port của MySQL database
- `MYSQL_DATABASE`: Tên database
- `MYSQL_USER`: Username MySQL
- `MYSQL_PASSWORD`: Password MySQL
- `JWT_SECRET_KEY`: Khóa bí mật cho JWT (Base64 encoded, phải giống với AuthService)

## Phát triển
- Sử dụng các branch riêng cho mỗi tính năng mới
- Tuân thủ code style của project
- Viết unit test cho các chức năng mới
- Sử dụng meaningful commit messages

## License
[MIT License](LICENSE) 