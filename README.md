# Health Application Backend Microservices

## Giới thiệu
Hệ thống backend của ứng dụng Health gồm bốn microservice chính:
- **APIService (API Gateway)**: Điểm vào trung tâm cho tất cả client requests, xử lý xác thực tập trung và định tuyến.
- **AuthService**: Quản lý đăng ký, đăng nhập, và đăng xuất người dùng.
- **UserService**: Quản lý thông tin người dùng và dữ liệu sức khỏe.
- **WorkoutService**: Quản lý bài tập, lịch tập, và các buổi tập đã lên lịch.

## Kiến trúc
Hệ thống được thiết kế theo kiến trúc microservice với các đặc điểm:
- Tách biệt database giữa các service
- Giao tiếp giữa các service qua RESTful API
- Xác thực tập trung thông qua API Gateway
- Kiểm tra token blacklist tập trung với Redis
- Tích hợp Eureka cho Service Discovery
- Circuit Breaker để tăng khả năng chịu lỗi
- Rate Limiting để bảo vệ khỏi tấn công DoS

## Cấu trúc hệ thống

```
health_backend/
├── apiservice/        # API Gateway Service
├── authservice/       # Authentication Service
├── discoveryserver/   # Eureka Service Discovery Server
├── userservice/       # User Management Service
└── workoutservice/    # Workout Management Service
```

## Luồng hoạt động

1. Client gửi request đến API Gateway (APIService)
2. APIService xác thực JWT token (nếu cần thiết)
3. Nếu token hợp lệ và không nằm trong blacklist, APIService định tuyến request đến service tương ứng
4. Service xử lý request và trả về response cho client thông qua APIService

### Luồng đăng ký và đăng nhập

1. Client gửi thông tin đăng ký đến `/auth/signup`
2. AuthService tạo tài khoản mới và gọi UserService để tạo profile người dùng
3. Khi đăng nhập qua `/auth/login`, AuthService trả về JWT token
4. JWT token được sử dụng trong các request tiếp theo
5. Khi đăng xuất, APIService đưa token vào blacklist trong Redis

## Kiến trúc xác thực tập trung

- JWT Authentication được xử lý tại API Gateway
- Token Blacklist được lưu trữ và kiểm tra tại Redis
- Các microservice bên trong không cần xác thực JWT, chỉ xử lý business logic
- Thông tin người dùng được trích xuất từ token và đính kèm vào headers của request

## Chi tiết từng Service

### 1. APIService (API Gateway)

**Chức năng**:
- Định tuyến request đến các service tương ứng
- Xác thực JWT token
- Kiểm tra token blacklist
- Rate limiting
- Circuit breaking
- Xử lý CORS

Xem chi tiết tại [APIService README](./apiservice/README.md)

### 2. AuthService

**Chức năng**:
- Đăng ký người dùng
- Đăng nhập và cấp token
- Đổi mật khẩu
- Đăng xuất (vô hiệu hóa token)

Xem chi tiết tại [AuthService README](./authservice/README.md)

### 3. UserService

**Chức năng**:
- Quản lý thông tin người dùng
- Cập nhật profile
- Lưu trữ dữ liệu sức khỏe cơ bản

Xem chi tiết tại [UserService README](./userservice/README.md)

### 4. WorkoutService

**Chức năng**:
- Quản lý thông tin bài tập
- Quản lý lịch tập
- Theo dõi tiến trình tập luyện
- Lên lịch và quản lý các buổi tập

Xem chi tiết tại [WorkoutService README](./workoutservice/README.md)

## Cài đặt và Chạy

### Yêu cầu hệ thống
- Java 21
- Maven
- Docker (cho MySQL và Redis)
- Docker Compose (tùy chọn)

### Cài đặt và cấu hình Eureka Server (Service Discovery)

Eureka Server giúp các microservice đăng ký và tìm kiếm lẫn nhau. Để cài đặt:

1. Tạo một project Spring Boot mới cho Eureka Server:
   ```
   mkdir discoveryserver
   cd discoveryserver
   ```

2. Tạo file `pom.xml` với nội dung:
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

3. Tạo thư mục cấu trúc:
   ```
   mkdir -p src/main/java/com/example/discoveryserver
   mkdir -p src/main/resources
   ```

4. Tạo file main application `src/main/java/com/example/discoveryserver/DiscoveryServerApplication.java`:
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

5. Tạo file cấu hình `src/main/resources/application.properties`:
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

6. Cập nhật parent pom.xml để bao gồm discovery server:
   ```xml
   <modules>
       <module>authservice</module>
       <module>userservice</module>
       <module>workoutservice</module>
       <module>apiservice</module>
       <module>discoveryserver</module>
   </modules>
   ```

### Cài đặt cơ sở dữ liệu

1. Khởi chạy Redis cho Token Blacklist:
   ```
   docker run --name redis -p 6379:6379 -d redis
   ```

2. Khởi chạy MySQL cho các service:
   ```
   docker run --name auth-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=auth_db -d mysql:8.0
   docker run --name user-mysql -p 3307:3306 -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=user_db -d mysql:8.0
   docker run --name workout-mysql -p 3309:3306 -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=workout_db -d mysql:8.0
   ```

### Khởi chạy các Service

1. Khởi chạy Eureka Server (Service Discovery):
   ```
   cd discoveryserver
   mvn spring-boot:run
   ```
   *Lưu ý*: Sau khi khởi động, bạn có thể kiểm tra Eureka Dashboard tại http://localhost:8761

2. Khởi chạy các service theo thứ tự:
   ```
   cd apiservice
   mvn spring-boot:run
   
   cd ../authservice
   mvn spring-boot:run
   
   cd ../userservice
   mvn spring-boot:run
   
   cd ../workoutservice
   mvn spring-boot:run
   ```

## Kiểm thử

Xem chi tiết về các test case trong README của từng service:
- [APIService Test Cases](./apiservice/README.md#test-cases-với-postman)
- [AuthService Test Cases](./authservice/README.md#thử-nghiệm-với-postman)
- [UserService Test Cases](./userservice/README.md#kiểm-thử-api-với-postman)
- [WorkoutService Test Cases](./workoutservice/README.md#thử-nghiệm-với-postman)

## Lưu ý

- Đảm bảo các service đã được đăng ký với Eureka Server khi khởi động
- Nếu Eureka Server gặp sự cố, các service vẫn có thể hoạt động nhưng sẽ không tìm thấy nhau
- Thay đổi các thông tin nhạy cảm (JWT secret, credentials) trước khi triển khai sản phẩm
- Xem xét triển khai HTTPS cho môi trường production 