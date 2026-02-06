# Ordering Application

A RESTful ordering system built with Spring Boot 4.0 and Java 25.

## Tech Stack

- **Java 25** (with Virtual Threads enabled)
- **Spring Boot 4.0.1**
- **Spring Data JPA** - Data persistence
- **MySQL** - Database
- **Lombok** - Reducing boilerplate code
- **Hibernate Validator** - Input validation
- **JUnit 5** - Testing

## Features

- **Customer Management** - Create, read, update customers
- **Employee Management** - CRUD operations with department support (code-based)
- **Item Catalog** - Product management with discount calculation
- **Order Processing** - Create and manage orders with multiple items
- **Order Delivery** - Mark orders as delivered
- **Employee Assignment** - Assign employees to work on orders (many-to-many)
- **Order Items** - Manage individual items within orders
- **Role-based Access Control** - Authorization via `X-User-Role` header
- **Global Exception Handling** - Consistent error responses
- **Profile-based Configuration** - Dev, test, stage, prod environments

## Department Codes

Employees are assigned to departments using numeric codes:

| Code | Department       | Description              |
|------|------------------|--------------------------|
| 1    | STORE            | Sales floor and warehouse |
| 2    | LAB              | Research and testing     |
| 3    | ACCOUNTING       | Finance and accounting   |
| 4    | ADMINISTRATION   | Management and HR        |

## Role-based Access Control

API endpoints are protected by roles. Include the `X-User-Role` header in requests:

```bash
curl -X GET http://localhost:8080/orders \
  -H "X-User-Role: EMPLOYEE_STORE"
```

**Example for customers:**
```bash
curl -X GET http://localhost:8080/customers \
  -H "X-User-Role: EMPLOYEE_STORE"
```

### Available Roles

| Role | Description |
|------|-------------|
| `CUSTOMER` | Create orders, view items, manage profile |
| `EMPLOYEE_STORE` | Full order management, delivery |
| `EMPLOYEE_LAB` | Item catalog CRUD |
| `EMPLOYEE_ADMINISTRATION` | Employee management |
| `EMPLOYEE_ACCOUNTING` | View orders, update prices |

See [API.md](API.md) for detailed permissions matrix.

## Prerequisites

- Java 25+
- MySQL 8.0+
- Gradle 9.x

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd ApplicationForOrdering
```

### 2. Configure Database

Make sure MySQL is running on `localhost:3306`.

Update credentials in `src/main/resources/application-dev.yaml` if needed:

```yaml
spring:
  datasource:
    url: "jdbc:mysql://localhost:3306/db_shop?createDatabaseIfNotExist=true"
    username: root
    password: root
```

### 3. Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### 4. Run with Different Profiles

```bash
# Development (default)
./gradlew bootRun

# Production
./gradlew bootRun --args='--spring.profiles.active=prod'

# Test
./gradlew bootRun --args='--spring.profiles.active=test'
```

## API Documentation

See [API.md](API.md) for complete API documentation with curl examples.

### Quick API Overview

| Resource | Endpoint | Methods |
|----------|----------|---------|
| Customers | `/customers` | GET, POST, PUT |
| Employees | `/employees` | GET, POST, PUT, DELETE |
| Items | `/items` | GET, POST, PUT, DELETE |
| Orders | `/orders` | GET, POST, PUT, DELETE |
| Order Items | `/order-items` | GET, POST, PUT, DELETE |

## Project Structure

```
src/main/java/org/example/company/
├── config/              # Configuration classes
├── controller/          # REST controllers
│   └── errorHandling/   # Global exception handlers
├── dto/
│   ├── request/         # Request DTOs
│   └── response/        # Response DTOs
├── exceptions/          # Custom exceptions
├── models/              # JPA entities
├── repository/          # Spring Data repositories
├── security/            # Role-based authorization
│   ├── annotation/      # @IsAllowedByRole
│   ├── aspect/          # AOP role checking
│   ├── config/          # Security configuration
│   ├── context/         # Role context (ThreadLocal)
│   ├── filter/          # X-User-Role header filter
│   └── model/           # Roles enum
└── service/             # Business logic
```

## Building

```bash
# Build the project
./gradlew build

# Build without tests
./gradlew build -x test

# Clean and build
./gradlew clean build
```

## Testing

```bash
# Run all tests
./gradlew test

# Test report will be generated at:
# build/reports/tests/test/index.html
```

## Configuration Files

| File | Description |
|------|-------------|
| `application.yaml` | Base configuration, active profile |
| `application-dev.yaml` | Development settings |
| `application-test.yaml` | Test environment |
| `application-stage.yaml` | Staging environment |
| `application-prod.yaml` | Production settings |

## Development

Hot reload is enabled via Spring Boot DevTools. Changes to Java files will automatically restart the application.

## Error Handling

The application returns consistent error responses:

```json
{
  "errorCode": "ERROR_CODE",
  "errorMessage": "Human readable message"
}
```

| HTTP Status | Description |
|-------------|-------------|
| 400 | Validation error / Invalid argument (e.g., invalid department code) |
| 403 | Forbidden - Access denied (missing or insufficient role) |
| 404 | Resource not found |
| 409 | Conflict (e.g., duplicate email) |
| 500 | Internal server error |

## License

This project is for educational purposes.