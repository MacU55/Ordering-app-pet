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

This API uses role-based access control via HTTP header. All protected endpoints require the `X-User-Role` header.

### X-User-Role Header

Include this header in every request to specify your role:

```
X-User-Role: EMPLOYEE_STORE
```

### Available Roles

| Role | Description | Access |
|------|-------------|--------|
| `CUSTOMER` | Customer/Client | Create orders, view items, manage own profile |
| `EMPLOYEE_STORE` | Store employee | Full order management, delivery, customer service |
| `EMPLOYEE_LAB` | Lab employee | Item catalog management (CRUD) |
| `EMPLOYEE_ADMINISTRATION` | Admin employee | Employee management, order assignment |
| `EMPLOYEE_ACCOUNTING` | Accounting employee | View orders, update item prices |

### Role Permissions Matrix

| Endpoint | CUSTOMER | STORE | LAB | ADMIN | ACCOUNTING |
|----------|----------|-------|-----|-------|------------|
| **Orders** |
| GET /orders | - | ✓ | - | ✓ | ✓ |
| GET /orders/{id} | ✓ | ✓ | - | ✓ | ✓ |
| POST /orders | ✓ | ✓ | - | - | - |
| PUT /orders/{id} | - | ✓ | - | - | - |
| DELETE /orders/{id} | - | ✓ | - | ✓ | - |
| POST /orders/{id}/deliver | - | ✓ | - | - | - |
| PATCH /orders/{id}/employee/{empId} | - | - | - | ✓ | - |
| **Items** |
| GET /items | ✓ | ✓ | ✓ | - | ✓ |
| GET /items/{id} | ✓ | ✓ | ✓ | - | ✓ |
| POST /items | - | - | ✓ | - | - |
| PUT /items/{id} | - | - | ✓ | - | ✓ |
| DELETE /items/{id} | - | - | ✓ | - | - |
| **Employees** |
| GET /employees | - | - | - | ✓ | - |
| POST /employees | - | - | - | ✓ | - |
| PUT /employees/{id} | - | - | - | ✓ | - |
| DELETE /employees/{id} | - | - | - | ✓ | - |
| **Customers** |
| GET /customers | - | ✓ | - | ✓ | - |
| GET /customers/{id} | ✓ | ✓ | - | ✓ | - |
| POST /customers | ✓ | ✓ | - | - | - |
| PUT /customers/{id} | ✓ | ✓ | - | ✓ | - |

### Example Requests with Role Header

**Bash/curl:**
```bash
curl -X GET http://localhost:8080/orders \
  -H "X-User-Role: EMPLOYEE_STORE"
```

**PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/orders" `
  -Method GET `
  -Headers @{"X-User-Role" = "EMPLOYEE_STORE"}
```

### Error Response (403 Forbidden)

If role is missing or insufficient:

```json
{
  "errorCode": "ACCESS_DENIED",
  "errorMessage": "AccessDeniedException : Role CUSTOMER is not allowed. Required roles: [EMPLOYEE_ADMINISTRATION]"
}
```

---

## Customers

### Get All Customers

```bash
curl -X GET http://localhost:8080/customers \
  -H "X-User-Role: EMPLOYEE_STORE"
```

### Get Customer by ID

```bash
curl -X GET http://localhost:8080/customers/1 \
  -H "X-User-Role: EMPLOYEE_STORE"
```

### Get Customer by Email

```bash
curl -X GET "http://localhost:8080/customers/by-email?email=john@example.com" \
  -H "X-User-Role: EMPLOYEE_STORE"
```

### Get Customer by Username

```bash
curl -X GET "http://localhost:8080/customers/by-username?userName=johndoe" \
  -H "X-User-Role: EMPLOYEE_STORE"
```

### Check if Email Exists

```bash
curl -X GET "http://localhost:8080/customers/email-exists?email=john@example.com" \
  -H "X-User-Role: CUSTOMER"
```

**Response:**
```json
{"exists": true}
```

### Create Customer

```bash
curl -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -H "X-User-Role: CUSTOMER" \
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
  -H "X-User-Role: EMPLOYEE_STORE" \
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "userName": "johnsmith",
    "email": "john.smith@example.com"
  }'
```

---

## Employees

### Get All Employees

```bash
curl -X GET http://localhost:8080/employees \
  -H "X-User-Role: EMPLOYEE_ADMINISTRATION"
```

### Get Employee by ID

```bash
curl -X GET http://localhost:8080/employees/1 \
  -H "X-User-Role: EMPLOYEE_ADMINISTRATION"
```

### Get Employee by Name

```bash
curl -X GET "http://localhost:8080/employees/by-name?name=Alice" \
  -H "X-User-Role: EMPLOYEE_ADMINISTRATION"
```

### Get Employee by Email

```bash
curl -X GET "http://localhost:8080/employees/by-email?email=alice@company.com" \
  -H "X-User-Role: EMPLOYEE_ADMINISTRATION"
```

### Get Employees by Department

Available departments: `STORE`, `LAB`, `ACCOUNTING`, `ADMINISTRATION`

```bash
curl -X GET "http://localhost:8080/employees/by-department?department=STORE" \
  -H "X-User-Role: EMPLOYEE_ADMINISTRATION"
```

### Get Department Info by Employee ID

```bash
curl -X GET http://localhost:8080/employees/1/department \
  -H "X-User-Role: EMPLOYEE_ADMINISTRATION"
```

**Response:**
```json
{
  "displayName": "Store",
  "code": 1,
  "description": "Sales floor and warehouse"
}
```

### Create Employee

Use department code (integer) instead of enum name:

| Code | Department       | Description              |
|------|------------------|--------------------------|
| 1    | STORE            | Sales floor and warehouse |
| 2    | LAB              | Research and testing     |
| 3    | ACCOUNTING       | Finance and accounting   |
| 4    | ADMINISTRATION   | Management and HR        |

```bash
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -H "X-User-Role: EMPLOYEE_ADMINISTRATION" \
  -d '{
    "name": "Alice Johnson",
    "salary": 55000.00,
    "email": "alice@company.com",
    "department": 1
  }'
```

> **Note:** Invalid department code returns `400 Bad Request` with error message.

### Update Employee

```bash
curl -X PUT http://localhost:8080/employees/1 \
  -H "Content-Type: application/json" \
  -H "X-User-Role: EMPLOYEE_ADMINISTRATION" \
  -d '{
    "name": "Alice Johnson",
    "salary": 60000.00,
    "email": "alice.johnson@company.com",
    "department": 4
  }'
```

### Delete Employee

```bash
curl -X DELETE http://localhost:8080/employees/1 \
  -H "X-User-Role: EMPLOYEE_ADMINISTRATION"
```

---

## Items

### Get All Items

```bash
curl -X GET http://localhost:8080/items \
  -H "X-User-Role: EMPLOYEE_LAB"
```

### Get Item by ID

```bash
curl -X GET http://localhost:8080/items/1 \
  -H "X-User-Role: EMPLOYEE_LAB"
```

### Get Item by Name

```bash
curl -X GET "http://localhost:8080/items/by-name?name=Laptop" \
  -H "X-User-Role: EMPLOYEE_LAB"
```

### Check if Item ID Exists

```bash
curl -X GET "http://localhost:8080/items/id-exists?id=1" \
  -H "X-User-Role: EMPLOYEE_LAB"
```

**Response:**
```json
{"exists": true}
```

### Get Discount Price for Item

```bash
curl -X GET http://localhost:8080/items/1/discount-price \
  -H "X-User-Role: EMPLOYEE_LAB"
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
  -H "X-User-Role: EMPLOYEE_LAB" \
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
  -H "X-User-Role: EMPLOYEE_LAB" \
  -d '{
    "name": "Laptop Pro",
    "description": "Premium high-performance laptop",
    "price": 1499.99,
    "itemType": "EQUIPMENT"
  }'
```

```bash
curl -X POST http://localhost:8080/items \
  -H "Content-Type: application/json" \
  -H "X-User-Role: EMPLOYEE_LAB" \
  -d '{
    "name": "Spagetti",
    "description": "delicious food",
    "price": 10.28,
    "itemType": "FOOD"
  }'
```


### Delete Item

```bash
curl -X DELETE http://localhost:8080/items/1 \
  -H "X-User-Role: EMPLOYEE_LAB"
```

---

## Orders

### Get All Orders

```bash
curl -X GET http://localhost:8080/orders \
  -H "X-User-Role: EMPLOYEE_STORE"
```

### Get Order by ID

```bash
curl -X GET http://localhost:8080/orders/1 \
  -H "X-User-Role: EMPLOYEE_STORE"
```

### Get Undelivered Orders

```bash
curl -X GET http://localhost:8080/orders/undelivered \
  -H "X-User-Role: EMPLOYEE_STORE"
```

### Get Orders Created After Date

Date format: ISO 8601 (`yyyy-MM-ddTHH:mm:ss`)

```bash
curl -X GET "http://localhost:8080/orders/created-after?date=2026-01-01T00:00:00" \
  -H "X-User-Role: EMPLOYEE_STORE"
```

### Get Orders Created Between Dates

```bash
curl -X GET "http://localhost:8080/orders/created-between?start=2026-01-01T00:00:00&end=2026-12-31T23:59:59" \
  -H "X-User-Role: EMPLOYEE_STORE"
```

### Create Order

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -H "X-User-Role: EMPLOYEE_STORE" \
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
  -H "X-User-Role: CUSTOMER" \
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
  -H "X-User-Role: EMPLOYEE_STORE" \
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
  -H "X-User-Role: EMPLOYEE_STORE"
```

### Deliver Order

Mark an order as delivered (sets `orderDateDelivered` to current timestamp).

```bash
curl -X POST http://localhost:8080/orders/1/deliver \
  -H "X-User-Role: EMPLOYEE_STORE"
```

**Response:** `204 No Content` on success.

### Assign Employee to Order

Add an employee to work on an order. One order can have multiple employees.

```bash
curl -X PATCH http://localhost:8080/orders/1/employee/5 \
  -H "X-User-Role: EMPLOYEE_ADMINISTRATION"
```

**Response:** `204 No Content` on success.

### Remove Employee from Order

Remove an employee from an order.

```bash
curl -X DELETE http://localhost:8080/orders/1/employee/5 \
  -H "X-User-Role: EMPLOYEE_ADMINISTRATION"
```

**Response:** `204 No Content` on success.

---

## Order Items

### Get All Order Items

```bash
curl -X GET http://localhost:8080/order-items
```

### Get Order Item by ID

```bash
curl -X GET http://localhost:8080/order-items/1
```

### Get Order Items by Order ID

```bash
curl -X GET http://localhost:8080/order-items/order/1
```

### Check if Order Item Exists

```bash
curl -X GET "http://localhost:8080/order-items/exists?orderId=1&itemId=1"
```

**Response:**
```json
{"exists": true}
```

### Create Order Item

```bash
curl -X POST http://localhost:8080/order-items \
  -H "Content-Type: application/json" \
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
  -d '{
    "quantity": 5,
    "price": 17.99
  }'
```

### Delete Order Item

```bash
curl -X DELETE http://localhost:8080/order-items/1
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
| 403  | Forbidden - Access denied (missing or insufficient role) |
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

Run all basic GET endpoints (using EMPLOYEE_ADMINISTRATION role for full access):

```bash
#!/bin/bash
BASE_URL="http://localhost:8080"
ROLE="X-User-Role: EMPLOYEE_ADMINISTRATION"

echo "=== Testing Customers ==="
curl -s -H "$ROLE" $BASE_URL/customers | head -c 200
echo -e "\n"

echo "=== Testing Employees ==="
curl -s -H "$ROLE" $BASE_URL/employees | head -c 200
echo -e "\n"

echo "=== Testing Items (as LAB) ==="
curl -s -H "X-User-Role: EMPLOYEE_LAB" $BASE_URL/items | head -c 200
echo -e "\n"

echo "=== Testing Orders (as STORE) ==="
curl -s -H "X-User-Role: EMPLOYEE_STORE" $BASE_URL/orders | head -c 200
echo -e "\n"

echo "=== Testing Order Items ==="
curl -s $BASE_URL/order-items | head -c 200
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
  -Headers @{"X-User-Role" = "EMPLOYEE_STORE"}
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
  -Headers @{"X-User-Role" = "EMPLOYEE_STORE"}
```

### DELETE Request

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/employees/1" `
  -Method DELETE `
  -Headers @{"X-User-Role" = "EMPLOYEE_ADMINISTRATION"}
```
