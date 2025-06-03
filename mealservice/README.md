# Meal Service

## Overview
Meal Service is a microservice responsible for managing meals, food items, and meal schedules in the Android Student application. This service handles the creation, retrieval, updating, and deletion of meals, as well as scheduling meals for specific times and tracking their status. The service features **automatic calorie calculation** where meal calories are synchronized with the sum of food calories.

## Features
- **Meal Management**: Complete CRUD operations for meals with automatic calorie calculation
- **Food Item Management**: Individual food items that compose meals with their own calorie values
- **Automatic Calorie Synchronization**: Meal calories are automatically calculated from food calories
- **Meal Scheduling**: Create and manage meal schedules for users with date ranges
- **Scheduled Meal Tracking**: Schedule specific meals for specific times with status tracking
- **Calorie Statistics**: Query calorie intake by day, week, month, or custom date range
- **Meal Types**: Support for BREAKFAST, LUNCH, DINNER, and SNACK
- **Meal Status Tracking**: SCHEDULED, COMPLETED, and CANCELLED status management
- **User-specific Meal Planning**: Filter meals and schedules by user ID and date ranges

## Technology Stack
- Java 21
- Spring Boot 3.2.3
- Spring Data JPA with Hibernate
- Spring Security (with stateless configuration)
- MySQL Database
- Spring Cloud Netflix Eureka Client
- Bean Validation (Jakarta Validation)

## Architecture

### Domain Model
- **Meal**: Core meal entity with automatic calorie calculation from associated foods
  - Properties: id, name, description, calories (auto-calculated), type
  - Relationships: One-to-Many with Food items
  - **Auto-sync**: Calories automatically updated when foods change
- **Food**: Individual food items that compose a meal
  - Properties: id, name, description, calories
  - Relationships: Many-to-One with Meal
  - **Auto-update**: Changes trigger meal calorie recalculation
- **Schedule**: Time-based meal planning for users
  - Properties: id, userId, name, description, startDate, endDate
  - Relationships: One-to-Many with ScheduledMeal
- **ScheduledMeal**: Links meals to specific schedules with timing and status
  - Properties: id, scheduledDateTime, status, notes
  - Relationships: Many-to-One with Schedule and Meal
- **MealType**: Enum (BREAKFAST, LUNCH, DINNER, SNACK)
- **MealStatus**: Enum (SCHEDULED, COMPLETED, CANCELLED)

### Service Layer Architecture
- **MealService**: Business logic for meal management with calorie validation
- **CalorieCalculationService**: Calorie statistics and calculation logic
- **CalorieValidationService**: Ensures calorie consistency between meals and foods
- **ScheduleService**: Schedule creation and management
- **ScheduledMealService**: Meal scheduling and status tracking
- **Mapper Pattern**: DTOs for data transfer between layers
- **Repository Pattern**: Data access abstraction with Spring Data JPA

## Setup and Configuration

### Prerequisites
- JDK 21
- Maven 3.6+
- MySQL 8.0+

### Database Configuration
The service connects to a MySQL database with the following configuration:
```properties
URL: jdbc:mysql://localhost:3310/meal_db
Username: root
Password: secret
Database Schema: Auto-created/updated via Hibernate DDL
```

### Service Configuration
- **Port**: 8008
- **Service Name**: mealservice
- **Security**: Stateless configuration (authentication handled at API Gateway)
- **CORS**: Enabled for all origins and common HTTP methods
- **Eureka Discovery**: Registers with Eureka server at http://localhost:8761/eureka/

## API Endpoints

### Meal Management
**Base URL**: `/meals`

- `POST /meals` - Create a new meal (calories auto-calculated from foods)
- `GET /meals` - Get all meals
- `GET /meals/{id}` - Get meal by ID
- `GET /meals/type/{type}` - Get meals by type (BREAKFAST, LUNCH, DINNER, SNACK)
- `GET /meals/search?name={name}` - Search meals by name
- `PUT /meals/{id}` - Update existing meal (calories auto-recalculated)
- `DELETE /meals/{id}` - Delete meal

### Calorie Statistics
**Base URL**: `/calories`

- `GET /calories/daily/{userId}?date={date}` - Get daily calorie intake
- `GET /calories/weekly/{userId}?startDate={date}` - Get weekly calorie intake (7 days)
- `GET /calories/monthly/{userId}?year={year}&month={month}` - Get monthly calorie intake
- `GET /calories/range/{userId}?startDate={date}&endDate={date}` - Get calories in custom date range

### Schedule Management
**Base URL**: `/meals/schedules`

- `POST /meals/schedules` - Create a new schedule
- `GET /meals/schedules/{id}` - Get schedule by ID
- `GET /meals/schedules/user/{userId}` - Get all schedules for a user
- `GET /meals/schedules/user/{userId}/date-range?startDate={date}&endDate={date}` - Get schedules in date range
- `PUT /meals/schedules/{id}` - Update existing schedule
- `DELETE /meals/schedules/{id}` - Delete schedule

### Scheduled Meal Management
**Base URL**: `/meals/scheduled-meals`

- `POST /meals/scheduled-meals` - Schedule a meal
- `GET /meals/scheduled-meals/{id}` - Get scheduled meal by ID
- `GET /meals/scheduled-meals/schedule/{scheduleId}` - Get all scheduled meals for a schedule
- `GET /meals/scheduled-meals/user/{userId}?startDateTime={datetime}&endDateTime={datetime}` - Get user's scheduled meals in date range
- `GET /meals/scheduled-meals/user/{userId}/status/{status}?startDateTime={datetime}&endDateTime={datetime}` - Get user's scheduled meals by status in date range
- `PUT /meals/scheduled-meals/{id}` - Update scheduled meal
- `PATCH /meals/scheduled-meals/{id}/status/{status}` - Update meal status only
- `DELETE /meals/scheduled-meals/{id}` - Cancel/delete scheduled meal

## JSON Request Examples

### Meal Management Examples

#### Create Meal with Auto-calculated Calories
```json
POST /meals
{
  "name": "Healthy Breakfast Bowl",
  "description": "Nutritious breakfast with automatic calorie calculation",
  "type": "BREAKFAST",
  "foods": [
    {
      "name": "Rolled Oats",
      "description": "Organic whole grain oats",
      "calories": 150
    },
    {
      "name": "Fresh Blueberries",
      "description": "Antioxidant-rich berries",
      "calories": 85
    },
    {
      "name": "Almonds",
      "description": "Raw unsalted almonds",
      "calories": 165
    }
  ]
}
```

**Response** (calories = 150 + 85 + 165 = 400):
```json
{
  "id": 1,
  "name": "Healthy Breakfast Bowl",
  "description": "Nutritious breakfast with automatic calorie calculation",
  "calories": 400,
  "type": "BREAKFAST",
  "foods": [...]
}
```

#### Create Simple Meal Without Foods
```json
POST /meals
{
  "name": "Simple Protein Shake",
  "description": "Quick protein drink",
  "calories": 200,
  "type": "SNACK"
}
```

#### Update Meal - Calories Auto-recalculated
```json
PUT /meals/{id}
{
  "name": "Enhanced Breakfast Bowl",
  "description": "Improved breakfast with extra nutrition",
  "type": "BREAKFAST",
  "foods": [
    {
      "id": 1,
      "name": "Rolled Oats",
      "calories": 150
    },
    {
      "id": 2,
      "name": "Fresh Blueberries",
      "calories": 85
    },
    {
      "id": 3,
      "name": "Almonds",
      "calories": 165
    },
    {
      "name": "Greek Yogurt",
      "description": "High protein yogurt",
      "calories": 100
    }
  ]
}
```

**Result**: Meal calories automatically updated to 500

### Calorie Statistics Examples

#### Daily Calories
```bash
GET /calories/daily/1001?date=2024-01-15
```

**Response**:
```json
{
  "userId": 1001,
  "date": "2024-01-15",
  "totalCalories": 1850
}
```

#### Weekly Calories
```bash
GET /calories/weekly/1001?startDate=2024-01-15
```

**Response**:
```json
[
  {
    "userId": 1001,
    "date": "2024-01-15",
    "totalCalories": 1850
  },
  {
    "userId": 1001,
    "date": "2024-01-16",
    "totalCalories": 1920
  }
  // ... 5 more days
]
```

#### Custom Range Calories
```bash
GET /calories/range/1001?startDate=2024-01-01&endDate=2024-01-31
```

### Schedule Management Examples

#### Create Schedule
```json
POST /meals/schedules
{
  "userId": 123,
  "name": "Weekly Meal Plan - January 2024",
  "description": "Healthy meal plan for the first week of January",
  "startDate": "2024-01-01",
  "endDate": "2024-01-07"
}
```

#### Update Schedule
```json
PUT /meals/schedules/{id}
{
  "userId": 123,
  "name": "Updated Weekly Meal Plan - January 2024",
  "description": "Modified healthy meal plan for the first week of January",
  "startDate": "2024-01-01",
  "endDate": "2024-01-10"
}
```

### Scheduled Meal Management Examples

#### Schedule a Meal
```json
POST /meals/scheduled-meals
{
  "scheduleId": 1,
  "mealId": 5,
  "scheduledDateTime": "2024-01-02T12:30:00",
  "status": "SCHEDULED",
  "notes": "Lunch at the office cafeteria"
}
```

#### Update Scheduled Meal
```json
PUT /meals/scheduled-meals/{id}
{
  "scheduleId": 1,
  "mealId": 8,
  "scheduledDateTime": "2024-01-02T13:00:00",
  "status": "SCHEDULED",
  "notes": "Changed to healthier option - moved time to 1 PM"
}
```

### Common Request Patterns

#### Meal Types Available
- `BREAKFAST`
- `LUNCH` 
- `DINNER`
- `SNACK`

#### Meal Status Available
- `SCHEDULED`
- `COMPLETED`
- `CANCELLED`

#### Date Format Examples
- **Date**: `"2024-01-15"` (ISO 8601 date format)
- **DateTime**: `"2024-01-15T14:30:00"` (ISO 8601 datetime format)

### Query Parameters Examples

#### Search Meals by Name
```
GET /meals/search?name=chicken
```

#### Get Schedules in Date Range
```
GET /meals/schedules/user/123/date-range?startDate=2024-01-01&endDate=2024-01-31
```

#### Get User's Scheduled Meals in Date Range
```
GET /meals/scheduled-meals/user/123?startDateTime=2024-01-01T00:00:00&endDateTime=2024-01-31T23:59:59
```

#### Get User's Scheduled Meals by Status
```
GET /meals/scheduled-meals/user/123/status/COMPLETED?startDateTime=2024-01-01T00:00:00&endDateTime=2024-01-31T23:59:59
```

### Response Format Examples

#### Successful Meal Creation Response (201 Created)
```json
{
  "id": 1,
  "name": "Healthy Breakfast Bowl",
  "description": "Nutritious breakfast with automatic calorie calculation",
  "calories": 400,
  "type": "BREAKFAST",
  "foods": [
    {
      "id": 1,
      "name": "Rolled Oats",
      "description": "Organic whole grain oats",
      "calories": 150
    },
    {
      "id": 2,
      "name": "Fresh Blueberries",
      "description": "Antioxidant-rich berries",
      "calories": 85
    }
  ]
}
```

#### Calorie Statistics Response
```json
{
  "userId": 1001,
  "date": "2024-01-15",
  "totalCalories": 1850
}
```

#### Error Response (400 Bad Request)
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/meals",
  "details": [
    {
      "field": "name",
      "message": "Meal name is required"
    },
    {
      "field": "calories",
      "message": "Calories must be a non-negative number"
    }
  ]
}
```

## Automatic Calorie Calculation

### Key Features
1. **Meal calories = Sum of food calories**: When foods are added/updated, meal calories auto-update
2. **Data Consistency**: Database-level validation ensures calorie accuracy
3. **Flexible Creation**: Can create meals with or without foods
4. **Real-time Updates**: Changes to food calories immediately update meal calories

### Workflow Examples

#### Standard Workflow
1. **Create meal with foods**: Calories calculated automatically
2. **Create schedule**: Plan meals for specific date range
3. **Schedule meals**: Assign meals to specific times
4. **Mark as completed**: Track actual food consumption
5. **Query statistics**: Get calorie intake analytics

#### Calorie Update Scenarios
- **Add food to meal**: Meal calories increase automatically
- **Remove food**: Meal calories decrease automatically  
- **Update food calories**: Meal calories recalculated
- **Replace foods**: New total calculated from new foods

## Workflow

### Typical Use Cases

1. **Meal Creation Workflow**:
   - Create individual food items with calorie values
   - Create meals and associate food items
   - Calories automatically calculated from foods
   - Set meal type for categorization

2. **Schedule Planning Workflow**:
   - Create a schedule for a user with date range
   - Schedule specific meals for specific times within the schedule
   - Track meal completion status

3. **Daily Meal Tracking Workflow**:
   - Query scheduled meals for current day
   - Update meal status as COMPLETED or CANCELLED
   - View calorie progress using statistics APIs

4. **Calorie Analytics Workflow**:
   - Query daily, weekly, monthly calorie intake
   - Use custom date ranges for specific analysis
   - Track calorie trends over time

## Building and Running

### Build the service:
```bash
./mvnw clean install
```

### Run the service:
```bash
./mvnw spring-boot:run
```

### Run with specific profile:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Data Transfer Objects (DTOs)
The service uses DTOs for clean separation between API and domain models:
- `MealDTO`: Meal data transfer with associated foods and auto-calculated calories
- `FoodDTO`: Individual food item data with calories
- `CalorieStatsDto`: Simplified calorie statistics (userId, date, totalCalories)
- `ScheduleDTO`: Schedule information with optional scheduled meals
- `ScheduledMealDTO`: Scheduled meal with meal details

## Security
- **Stateless Configuration**: No session management
- **CORS Enabled**: Cross-origin requests allowed
- **Gateway Authentication**: Authentication handled at API Gateway level
- **All Endpoints Public**: Internal service assumes pre-authenticated requests

## Integration
This service is part of a microservice architecture:
- **Service Discovery**: Registers with Netflix Eureka
- **API Gateway**: Routes requests through central gateway
- **Database**: Dedicated MySQL database for meal data
- **Inter-service Communication**: RESTful APIs for service-to-service calls

## Error Handling
- **ResourceNotFoundException**: For non-existent entities
- **Validation Errors**: Bean validation with detailed error messages
- **Calorie Consistency Errors**: Automatic synchronization prevents inconsistencies
- **Standardized Response Format**: Consistent error response structure

## Monitoring and Observability
- **Spring Boot Actuator**: Health checks and metrics
- **Eureka Registration**: Service health monitoring
- **Structured Logging**: Consistent log format for debugging
- **Calorie Audit Trail**: Track calorie calculation changes 