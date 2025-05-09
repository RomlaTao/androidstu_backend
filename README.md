# Health Application Backend Microservices

## Giới thiệu
Hệ thống backend của ứng dụng Health gồm hai microservice chính:
- **AuthService**: Quản lý xác thực và bảo mật
- **UserService**: Quản lý thông tin người dùng và dữ liệu sức khỏe

## Kiến trúc
Hệ thống được thiết kế theo kiến trúc microservice với các đặc điểm:
- Tách biệt database giữa các service
- Giao tiếp giữa các service qua RESTful API
- Xác thực thông qua JWT
- Bảo mật người dùng với Spring Security

## Tổng hợp các Endpoint API của cả hai service

### I. AuthService Endpoints

#### 1. Đăng ký tài khoản
- **URL**: `/auth/signup`
- **Method**: POST
- **Request Body**:
```json
{
  "fullName": "string",
  "email": "string",
  "password": "string"
}
```
- **Response**: User object với thông tin đã đăng ký
```json
{
  "id": "number",
  "fullName": "string",
  "email": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

#### 2. Đăng nhập
- **URL**: `/auth/login`
- **Method**: POST
- **Request Body**:
```json
{
  "email": "string",
  "password": "string"
}
```
- **Response**:
```json
{
  "token": "string",
  "expiresIn": "number"
}
```

#### 3. Đổi mật khẩu
- **URL**: `/auth/change-password`
- **Method**: POST
- **Headers**: Authorization: Bearer JWT_TOKEN
- **Request Body**:
```json
{
  "currentPassword": "string",
  "newPassword": "string"
}
```
- **Response**:
```json
{
  "message": "Password changed successfully",
  "token": "string",
  "expiresIn": "number"
}
```

#### 4. Đăng xuất
- **URL**: `/auth/logout`
- **Method**: POST
- **Headers**: Authorization: Bearer JWT_TOKEN
- **Response**:
```json
{
  "message": "Logged out successfully"
}
```

### II. UserService Endpoints

#### 1. Lấy danh sách tất cả người dùng
- **URL**: `/api/users`
- **Method**: GET
- **Headers**: Authorization: Bearer JWT_TOKEN
- **Response**: Danh sách UserDto objects
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

#### 2. Lấy thông tin người dùng theo ID
- **URL**: `/api/users/{id}`
- **Method**: GET
- **Headers**: Authorization: Bearer JWT_TOKEN
- **Response**: UserDto object
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

#### 3. Lấy thông tin người dùng theo email
- **URL**: `/api/users/email/{email}`
- **Method**: GET
- **Headers**: Authorization: Bearer JWT_TOKEN
- **Response**: UserDto object (định dạng như trên)

#### 4. Tạo người dùng mới (chủ yếu sử dụng bởi AuthService)
- **URL**: `/api/users`
- **Method**: POST
- **Headers**: Authorization: Bearer JWT_TOKEN
- **Request Body**:
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
- **Response**: UserDto object đã tạo (định dạng như trên)

#### 5. Cập nhật thông tin người dùng
- **URL**: `/api/users/{id}`
- **Method**: PUT
- **Headers**: Authorization: Bearer JWT_TOKEN
- **Request Body**:
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
- **Lưu ý**: Không thể cập nhật email và password qua endpoint này
- **Response**: UserDto object đã cập nhật (định dạng như trên)

#### 6. Hướng dẫn về thay đổi thông tin bảo mật
- **URL**: `/api/users/{id}/security`
- **Method**: PUT
- **Headers**: Authorization: Bearer JWT_TOKEN
- **Response**:
```json
{
  "message": "Để thay đổi email và mật khẩu, vui lòng sử dụng AuthService",
  "changePasswordEndpoint": "/auth/change-password"
}
```

#### 7. Xóa người dùng
- **URL**: `/api/users/{id}`
- **Method**: DELETE
- **Headers**: Authorization: Bearer JWT_TOKEN
- **Response**: HTTP 204 No Content

## Cài đặt và Chạy
Chi tiết về cài đặt và chạy mỗi service vui lòng xem README trong thư mục tương ứng:
- [AuthService README](./authservice/README.md)
- [UserService README](./userservice/README.md)

## Quy trình hoạt động
1. Người dùng đăng ký qua AuthService
2. AuthService tạo tài khoản và gọi UserService tạo profile người dùng
3. Khi đăng nhập, AuthService trả về JWT token
4. JWT token được sử dụng để xác thực các request đến UserService
5. Khi đổi mật khẩu, một token mới được tạo
6. Khi đăng xuất, token sẽ bị đưa vào blacklist 