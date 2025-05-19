# Meal Service

## Overview
Meal Service is a microservice responsible for managing meals, food items, and meal schedules in the Android Student application. This service handles the creation, retrieval, updating, and deletion of meals, as well as scheduling meals for specific times and tracking their status.

## Features
- Meal management (create, read, update, delete operations)
- Food item management as part of meals
- Meal scheduling functionality
- Different meal types (breakfast, lunch, dinner, snack)
- Tracking of meal status (scheduled, completed, cancelled)
- Nutritional information tracking (carbohydrates, proteins, lipids)

## Technology Stack
- Java 21
- Spring Boot 3.2.3
- Spring Data JPA
- Spring Security
- MySQL Database
- Spring Cloud (Netflix Eureka Client)

## Setup and Configuration
### Prerequisites
- JDK 21
- Maven
- MySQL database

### Database Configuration
The service connects to a MySQL database with the following default configuration:
```
URL: jdbc:mysql://localhost:3310/meal_db
Username: root
Password: secret
```

You can modify these settings in the `application.properties` file.

### Service Configuration
- Default port: 8008
- Service name: mealservice
- Eureka client enabled (connects to Eureka server at http://localhost:8761/eureka/)

## Building and Running
To build the service:
```
./mvnw clean install
```

To run the service:
```
./mvnw spring-boot:run
```

## API Endpoints
The service exposes RESTful endpoints for:
- `/api/meals` - Meal CRUD operations
- `/api/schedules` - Schedule management
- `/api/scheduled-meals` - Operations on scheduled meals

## Domain Model
- **Meal**: Represents a meal with nutritional information and associated food items
- **Food**: Represents individual food items that compose a meal
- **Schedule**: Represents a time plan for meals
- **ScheduledMeal**: Represents a meal scheduled for a specific time with status tracking
- **MealType**: Enum for meal types (BREAKFAST, LUNCH, DINNER, SNACK)
- **MealStatus**: Enum for tracking meal status (SCHEDULED, COMPLETED, CANCELLED)

## Integration
This service is part of a microservice architecture and registers with a Eureka discovery server, allowing other services to locate and communicate with it. 