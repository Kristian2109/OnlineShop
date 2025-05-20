# Online Shop - Order API Postman Collection

This Postman collection includes requests for testing all the endpoints in the Order API of the Online Shop application.

## Prerequisites

- [Postman](https://www.postman.com/downloads/) installed
- The Online Shop application running locally or on a server

## Setting up the Collection

1. Open Postman
2. Click on "Import" and select the `Order_API_Collection.json` file
3. The collection will be added to your Postman workspace

## Configuring Variables

The collection uses variables that need to be set before running the requests:

1. Click on the collection name "Online Shop - Order API"
2. Go to the "Variables" tab
3. Set the following variables:
   - `base_url`: The base URL of your API (default: `http://localhost:8080`)
   - `auth_token`: A valid JWT token for a regular user
   - `admin_token`: A valid JWT token for an admin user

## Obtaining Authentication Tokens

To get the JWT tokens required for authentication:

1. Use the login endpoint in your application:
   ```
   POST /api/auth/login
   Content-Type: application/json
   
   {
       "username": "your_username",
       "password": "your_password"
   }
   ```

2. The response will contain a token that should be set as the `auth_token` or `admin_token` variable depending on the user's role.

## Using the Collection

The collection is organized into two folders:

### User Operations

These endpoints can be accessed by regular users:

- **Create a new order**: POST `/api/orders`
- **Get order by ID**: GET `/api/orders/{orderId}`
- **Get orders by customer ID**: GET `/api/orders/customer/{customerId}`

### Admin Operations

These endpoints can only be accessed by administrators:

- **Get all orders**: GET `/api/orders`
- **Get orders by status**: GET `/api/orders/status/{status}`
- **Get orders by date range**: GET `/api/orders/date-range`
- **Update order status**: PUT `/api/orders/{orderId}/status`
- **Delete order**: DELETE `/api/orders/{orderId}`

## Testing Tips

- For date range queries, use ISO 8601 date format (`YYYY-MM-DDTHH:MM:SSZ`)
- Order status values should match the enum values in the backend (`PENDING`, `CONFIRMED`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`, `REFUNDED`)
- For pagination, `page` is zero-based (first page is 0)
- For sorting, you can use `sortOrder=asc` or `sortOrder=desc` 