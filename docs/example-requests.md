# Example Requests & Responses

## Authentication

### Register New User

Request:

```http
POST http://localhost:8091/api/auth/register
Content-Type: application/json

{
    "name": "John Doe",
    "phone": "081234567890",
    "username": "johndoe",
    "password": "password123"
}
```

Success Response:

```json
{
  "status": "Success",
  "message": "User registered successfully",
  "data": {
    "username": "johndoe",
    "role": "ROLE_USER"
  }
}
```

Error Response (Username exists):

```json
{
  "status": "Error",
  "message": "Username already exists"
}
```

### Login

Request:

```http
POST http://localhost:8091/api/auth/login
Content-Type: application/json

{
    "username": "johndoe",
    "password": "password123"
}
```

Success Response:

```json
{
  "status": "Success",
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "username": "johndoe",
  "role": "ROLE_USER"
}
```

Error Response (Invalid credentials):

```json
{
  "status": "Error",
  "message": "Invalid username or password"
}
```

## User Endpoints

### Get Profile

Request:

```http
GET http://localhost:8091/api/user/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

Success Response:

```json
{
  "status": "Success",
  "data": {
    "id": 1,
    "name": "John Doe",
    "username": "johndoe",
    "phone": "081234567890",
    "role": "ROLE_USER"
  }
}
```

### Update Profile

Request:

```http
PUT http://localhost:8091/api/user/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
Content-Type: application/json

{
    "name": "John Doe Updated",
    "phone": "081234567891",
    "password": "newpassword123"
}
```

Success Response:

```json
{
  "status": "Success",
  "message": "Profile updated successfully",
  "data": {
    "name": "John Doe Updated",
    "username": "johndoe",
    "phone": "081234567891"
  }
}
```

## Admin Endpoints

### Get All Users (with pagination)

Request:

```http
GET http://localhost:8091/api/admin/users?page=0&size=10&sort=username&direction=asc
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

Success Response:

```json
{
  "status": "Success",
  "data": [
    {
      "id": 1,
      "name": "John Doe",
      "username": "johndoe",
      "phone": "081234567890",
      "role": "ROLE_USER"
    },
    {
      "id": 2,
      "name": "Jane Doe",
      "username": "janedoe",
      "phone": "081234567891",
      "role": "ROLE_USER"
    }
  ],
  "pagination": {
    "page": 0,
    "size": 10,
    "totalElements": 2,
    "totalPages": 1
  }
}
```

### Create New Admin

Request:

```http
POST http://localhost:8091/api/admin/create
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
Content-Type: application/json

{
    "name": "Admin User",
    "phone": "081234567890",
    "username": "admin2",
    "password": "admin123"
}
```

Success Response:

```json
{
  "status": "Success",
  "message": "Admin registered successfully",
  "data": {
    "username": "admin2",
    "role": "ROLE_ADMIN"
  }
}
```

### Update User by Admin

Request:

```http
PUT http://localhost:8091/api/admin/user/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
Content-Type: application/json

{
    "name": "Updated By Admin",
    "phone": "081234567899",
    "password": "newpass123"
}
```

Success Response:

```json
{
  "status": "Success",
  "message": "User updated successfully",
  "data": {
    "id": 1,
    "name": "Updated By Admin",
    "username": "johndoe",
    "phone": "081234567899",
    "role": "ROLE_USER"
  }
}
```

## Common Error Responses

### Unauthorized (No token/Invalid token)

```json
{
  "timestamp": "2025-06-10T07:30:00.000+0000",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid token",
  "path": "/api/user/profile"
}
```

### Forbidden (Wrong role)

```json
{
  "timestamp": "2025-06-10T07:30:00.000+0000",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied",
  "path": "/api/admin/users"
}
```

### Bad Request (Invalid data)

```json
{
  "status": "Error",
  "message": "Invalid request data",
  "errors": {
    "username": "Username is required",
    "password": "Password must be at least 6 characters"
  }
}
```
