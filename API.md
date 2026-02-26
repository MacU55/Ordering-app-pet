# Ordering Application API Documentation

Base URL: `http://localhost:8080`

---

## Table of Contents

- [Authentication](#authentication)
- [Customers](#customers)
- [Employees](#employees)
- [Items](#items)
- [Orders](#orders)
- [Order Items](#order-items)
- [Utility](#utility)

---

## Authentication

This API uses role-based access control. User identity and roles are determined by the `X-User-Email` header. The backend looks up the user by email in the database and assigns roles accordingly.

### X-User-Email Header

Include this header in every request with the user's email address. The system determines whether the email belongs to an **employee** (domain `@ordering.com`) or a **customer** (any other domain), then loads roles from the database.

```
X-User-Email: bob@ordering.com
```

For customers:
```
X-User-Email: john@example.com
```

### Available Roles

| Role | Description | Who gets it |
|------|-------------|-------------|
| `CUSTOMER` | Customer/Client | Users found in `customers` table |
| `EMPLOYEE_STORE` | Store employee | Employees with Store role |
| `EMPLOYEE_LAB` | Lab employee | Employees with Lab role |
| `EMPLOYEE_ADMIN` | Admin employee | Employees with Admin role |
| `EMPLOYEE_ACCOUNTING` | Accounting employee | Employees with Accounting role |

### Role Permissions Matrix

| Endpoint | CUSTOMER | STORE | LAB | ADMIN | ACCOUNTING |
|----------|----------|-------|-----|-------|------------|
| **Orders** |
| GET /orders | - | ✓ | - | ✓ | ✓ |
| GET /orders/{id} | - | ✓ | - | ✓ | ✓ |
| GET /orders/undelivered | - | ✓ | - | ✓ | - |
| GET /orders/created-after | - | ✓ | - | ✓ | ✓ |
| GET /orders/created-between | - | ✓ | - | ✓ | ✓ |
| POST /orders | ✓ | ✓ | - | - | - |
| PUT /orders/{id} | - | ✓ | - | - | - |
| DELETE /orders/{id} | - | ✓ | - | ✓ | - |
| POST /orders/{id}/deliver | - | ✓ | - | - | - |
| PATCH /orders/{id}/employee/{empId} | - | - | - | ✓ | - |
| DELETE /orders/{id}/employee/{empId} | - | - | - | ✓ | - |
| **Items** |
| GET /items | ✓ | ✓ | ✓ | - | ✓ |
| GET /items/{id} | ✓ | ✓ | ✓ | - | ✓ |
| GET /items/by-name | ✓ | ✓ | ✓ | - | - |
| GET /items/id-exists | - | ✓ | ✓ | - | - |
| GET /items/{id}/discount-price | - | ✓ | ✓ | - | ✓ |
| POST /items | - | - | ✓ | - | - |
| PUT /items/{id} | - | - | ✓ | - | ✓ |
| DELETE /items/{id} | - | - | ✓ | - | - |
| **Employees** |
| GET /employees | - | - | - | ✓ | - |
| GET /employees/{id} | - | - | - | ✓ | - |
| GET /employees/by-name | - | - | - | ✓ | - |
| GET /employees/by-email | - | - | - | ✓ | - |
| POST /employees | - | - | - | ✓ | - |
| PUT /employees/{id} | - | - | - | ✓ | - |
| DELETE /employees/{id} | - | - | - | ✓ | - |
| **Customers** |
| GET /customers | - | ✓ | - | ✓ | - |
| GET /customers/{id} | ✓ | ✓ | - | ✓ | - |
| GET /customers/by-email | - | ✓ | - | ✓ | - |
| GET /customers/by-username | - | ✓ | - | ✓ | - |
| GET /customers/email-exists | ✓ | ✓ | - | ✓ | ✓ |
| POST /customers | ✓ | ✓ | - | - | - |
| PUT /customers/{id} | ✓ | ✓ | - | ✓ | - |
| **Order Items** |
| All /order-items endpoints | - | - | - | - | ✓ |

### Example Requests with Email Header

**Bash/curl:**
```bash
curl -X GET http://localhost:8080/orders \
  -H "X-User-Email: alice@ordering.com"
```

**PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/orders" `
  -Method GET `
  -Headers @{"X-User-Email" = "alice@ordering.com"}
```

### Error Response (403 Forbidden)

If the email is missing, not found, or the user's roles are insufficient:

```json
{
  "errorCode": "ACCESS_DENIED",
  "errorMessage": "Access Denied"
}
```

---

## Customers

### Get All Customers

```bash
curl -X GET http://localhost:8080/customers \
  -H "X-User-Email: alice@ordering.com"
```

### Get Customer by ID

```bash
curl -X GET http://localhost:8080/customers/1 \
  -H "X-User-Email: alice@ordering.com"
```

### Get Customer by Email

```bash
curl -X GET "http://localhost:8080/customers/by-email?email=john@example.com" \
  -H "X-User-Email: alice@ordering.com"
```

### Get Customer by Username

```bash
curl -X GET "http://localhost:8080/customers/by-username?userName=johndoe" \
  -H "X-User-Email: alice@ordering.com"
```

### Check if Email Exists

```bash
curl -X GET "http://localhost:8080/customers/email-exists?email=john@example.com" \
  -H "X-User-Email: john@example.com"
```

**Response:**
```json
{"exists": true}
```

### Create Customer

```bash
curl -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -H "X-User-Email: john@example.com" \
  -d '{
    "firstName": "Bob",
    "lastName": "Boot",
    "userName": "bobboot",
    "email": "bobboot@example.com"
  }'
```

### Update Customer

```bash
curl -X PUT http://localhost:8080/customers/1 \
  -H "Content-Type: application/json" \
  -H "X-User-Email: alice@ordering.com" \
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "userName": "johnsmith",
    "email": "john.smith@example.com"
  }'
```

---

## Employees

Employee endpoints require `EMPLOYEE_ADMIN` role. Use an admin employee's email (e.g. `alice@ordering.com`).

### Get All Employees

```bash
curl -X GET http://localhost:8080/employees \
  -H "X-User-Email: alice@ordering.com"
```

### Get Employee by ID

```bash
curl -X GET http://localhost:8080/employees/1 \
  -H "X-User-Email: alice@ordering.com"
```

### Get Employee by Name

```bash
curl -X GET "http://localhost:8080/employees/by-name?name=Alice" \
  -H "X-User-Email: alice@ordering.com"
```

### Get Employee by Email

```bash
curl -X GET "http://localhost:8080/employees/by-email?email=alice@ordering.com" \
  -H "X-User-Email: alice@ordering.com"
```

### Create Employee

Email is auto-generated from the employee name (e.g. `Alice Johnson` → `AliceJohnson@ordering.com`). If the base email exists, a numeric suffix is added (`AliceJohnson2@ordering.com`).

```bash
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -H "X-User-Email: alice@ordering.com" \
  -d '{
    "name": "Alice Johnson",
    "salary": 55000.00
  }'
```

### Update Employee

```bash
curl -X PUT http://localhost:8080/employees/1 \
  -H "Content-Type: application/json" \
  -H "X-User-Email: alice@ordering.com" \
  -d '{
    "name": "Alice Johnson",
    "salary": 60000.00
  }'
```

### Delete Employee

```bash
curl -X DELETE http://localhost:8080/employees/1 \
  -H "X-User-Email: alice@ordering.com"
```

---

## Items

### Get All Items

```bash
curl -X GET http://localhost:8080/items \
  -H "X-User-Email: lab@ordering.com"
```

### Get Item by ID

```bash
curl -X GET http://localhost:8080/items/1 \
  -H "X-User-Email: lab@ordering.com"
```

### Get Item by Name

```bash
curl -X GET "http://localhost:8080/items/by-name?name=Laptop" \
  -H "X-User-Email: lab@ordering.com"
```

### Check if Item ID Exists

```bash
curl -X GET "http://localhost:8080/items/id-exists?id=1" \
  -H "X-User-Email: lab@ordering.com"
```

**Response:**
```json
{"exists": true}
```

### Get Discount Price for Item

```bash
curl -X GET http://localhost:8080/items/1/discount-price \
  -H "X-User-Email: lab@ordering.com"
```

### Create Item

Available item types:

| ItemType    | Discount |
|-------------|----------|
| `FOOD`      | 5%       |
| `EQUIPMENT` | 6%       |
| `CLOTHES`   | 7%       |
| `UNDEFINED` | 8%       |

```bash
curl -X POST http://localhost:8080/items \
  -H "Content-Type: application/json" \
  -H "X-User-Email: lab@ordering.com" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1299.99,
    "itemType": "EQUIPMENT"
  }'
```

### Update Item

```bash
curl -X PUT http://localhost:8080/items/1 \
  -H "Content-Type: application/json" \
  -H "X-User-Email: lab@ordering.com" \
  -d '{
    "name": "Laptop Pro",
    "description": "Premium high-performance laptop",
    "price": 1499.99,
    "itemType": "EQUIPMENT"
  }'
```

### Delete Item

```bash
curl -X DELETE http://localhost:8080/items/1 \
  -H "X-User-Email: lab@ordering.com"
```

---

## Orders

### Get All Orders

```bash
curl -X GET http://localhost:8080/orders \
  -H "X-User-Email: store@ordering.com"
```

### Get Order by ID

```bash
curl -X GET http://localhost:8080/orders/1 \
  -H "X-User-Email: store@ordering.com"
```

### Get Undelivered Orders

```bash
curl -X GET http://localhost:8080/orders/undelivered \
  -H "X-User-Email: store@ordering.com"
```

### Get Orders Created After Date

Date format: ISO 8601 (`yyyy-MM-ddTHH:mm:ss`)

```bash
curl -X GET "http://localhost:8080/orders/created-after?date=2026-01-01T00:00:00" \
  -H "X-User-Email: store@ordering.com"
```

### Get Orders Created Between Dates

```bash
curl -X GET "http://localhost:8080/orders/created-between?start=2026-01-01T00:00:00&end=2026-12-31T23:59:59" \
  -H "X-User-Email: store@ordering.com"
```

### Create Order

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -H "X-User-Email: store@ordering.com" \
  -d '{
    "items": [
      {
        "itemId": 1,
        "quantity": 2,
        "price": 29.99
      },
      {
        "itemId": 2,
        "quantity": 1,
        "price": 49.99
      }
    ],
    "customerId" : 1
  }'
```

### Creating Order without price

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -H "X-User-Email: john@example.com" \
  -d '{
    "items": [
      {
        "itemId": 1,
        "quantity": 10
      },
      {
        "itemId": 2,
        "quantity": 150
      }
    ],
    "customerId" : 1
  }'
```

### Update Order

```bash
curl -X PUT http://localhost:8080/orders/1 \
  -H "Content-Type: application/json" \
  -H "X-User-Email: store@ordering.com" \
  -d '{
    "items": [
      {
        "itemId": 1,
        "quantity": 3,
        "price": 29.99
      }
    ]
  }'
```

### Delete Order

```bash
curl -X DELETE http://localhost:8080/orders/1 \
  -H "X-User-Email: store@ordering.com"
```

### Deliver Order

Mark an order as delivered (sets `orderDateDelivered` to current timestamp).

```bash
curl -X POST http://localhost:8080/orders/1/deliver \
  -H "X-User-Email: store@ordering.com"
```

**Response:** `204 No Content` on success.

### Assign Employee to Order

Add an employee to work on an order. One order can have multiple employees. Requires `EMPLOYEE_ADMIN` role.

```bash
curl -X PATCH http://localhost:8080/orders/1/employee/5 \
  -H "X-User-Email: alice@ordering.com"
```

**Response:** `204 No Content` on success.

### Remove Employee from Order

Remove an employee from an order. Requires `EMPLOYEE_ADMIN` role.

```bash
curl -X DELETE http://localhost:8080/orders/1/employee/5 \
  -H "X-User-Email: alice@ordering.com"
```

**Response:** `204 No Content` on success.

---

## Order Items

Order Items endpoints require `EMPLOYEE_ACCOUNTING` role.

### Get All Order Items

```bash
curl -X GET http://localhost:8080/order-items \
  -H "X-User-Email: accounting@ordering.com"
```

### Get Order Item by ID

```bash
curl -X GET http://localhost:8080/order-items/1 \
  -H "X-User-Email: accounting@ordering.com"
```

### Get Order Items by Order ID

```bash
curl -X GET http://localhost:8080/order-items/order/1 \
  -H "X-User-Email: accounting@ordering.com"
```

### Check if Order Item Exists

```bash
curl -X GET "http://localhost:8080/order-items/exists?orderId=1&itemId=1" \
  -H "X-User-Email: accounting@ordering.com"
```

**Response:**
```json
{"exists": true}
```

### Create Order Item

```bash
curl -X POST http://localhost:8080/order-items \
  -H "Content-Type: application/json" \
  -H "X-User-Email: accounting@ordering.com" \
  -d '{
    "orderId": 1,
    "itemId": 2,
    "quantity": 3,
    "price": 19.99
  }'
```

### Update Order Item

```bash
curl -X PUT http://localhost:8080/order-items/1 \
  -H "Content-Type: application/json" \
  -H "X-User-Email: accounting@ordering.com" \
  -d '{
    "quantity": 5,
    "price": 17.99
  }'
```

### Delete Order Item

```bash
curl -X DELETE http://localhost:8080/order-items/1 \
  -H "X-User-Email: accounting@ordering.com"
```

---

## Utility

> **Note:** This controller is only available when `company.utility.enabled=true` in configuration.

### Get Main Info

```bash
curl -X GET http://localhost:8080/utility/mainInfo
```

### Send Notification

```bash
curl -X GET http://localhost:8080/utility/notification/HelloWorld
```

---

## Error Responses

The API returns standard HTTP status codes:

| Code | Description |
|------|-------------|
| 200  | OK - Request successful |
| 201  | Created - Resource created successfully |
| 204  | No Content - Resource deleted successfully |
| 400  | Bad Request - Validation error or invalid argument |
| 403  | Forbidden - Access denied (missing header, unknown email, or insufficient role) |
| 404  | Not Found - Resource not found |
| 409  | Conflict - Duplicate entry (e.g., email already exists) |
| 500  | Internal Server Error |

### Invalid Argument Example

```json
{
  "errorCode": "INVALID_ARGUMENT",
  "errorMessage": "Invalid Department Code: 99"
}
```

### Validation Error Example

```json
{
  "timestamp": "2026-02-02T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "email is mandatory"
    }
  ]
}
```

---

## Quick Test Script

Run all basic GET endpoints. Use an admin employee email for employees/customers, store for orders, lab for items, accounting for order-items:

```bash
#!/bin/bash
BASE_URL="http://localhost:8080"
ADMIN_EMAIL="alice@ordering.com"

echo "=== Testing Customers ==="
curl -s -H "X-User-Email: $ADMIN_EMAIL" $BASE_URL/customers | head -c 200
echo -e "\n"

echo "=== Testing Employees ==="
curl -s -H "X-User-Email: $ADMIN_EMAIL" $BASE_URL/employees | head -c 200
echo -e "\n"

echo "=== Testing Items ==="
curl -s -H "X-User-Email: lab@ordering.com" $BASE_URL/items | head -c 200
echo -e "\n"

echo "=== Testing Orders ==="
curl -s -H "X-User-Email: store@ordering.com" $BASE_URL/orders | head -c 200
echo -e "\n"

echo "=== Testing Order Items ==="
curl -s -H "X-User-Email: accounting@ordering.com" $BASE_URL/order-items | head -c 200
echo -e "\n"

echo "=== All tests completed ==="
```

---

## Windows PowerShell Examples

If using PowerShell instead of bash:

### GET Request

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/customers" `
  -Method GET `
  -Headers @{"X-User-Email" = "alice@ordering.com"}
```

### POST Request

```powershell
$body = @{
    firstName = "John"
    lastName = "Doe"
    userName = "johndoe"
    email = "john@example.com"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/customers" `
  -Method POST `
  -Body $body `
  -ContentType "application/json" `
  -Headers @{"X-User-Email" = "alice@ordering.com"}
```

### DELETE Request

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/employees/1" `
  -Method DELETE `
  -Headers @{"X-User-Email" = "alice@ordering.com"}
```
