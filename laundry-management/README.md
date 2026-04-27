# 🧺 Laundry Order Management System

A lightweight backend system for dry cleaning stores to manage 
daily orders, track garment status, and monitor business performance.

Built with Java Spring Boot as part of an internship assignment.

---

## Tech Stack

- Java 17
- Spring Boot 3.5
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven

---

## Setup Instructions

### Prerequisites
- Java 17+
- PostgreSQL installed and running
- Maven

### Steps

1. Clone the repository
   git clone <your-repo-url>
   cd laundry-management

2. Create PostgreSQL database
   CREATE DATABASE laundrydb;

3. Update application.properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/laundrydb
   spring.datasource.username=postgres
   spring.datasource.password=yourpassword

4. Run the project
   mvnw.cmd spring-boot:run

5. Server starts at http://localhost:8080

---

## Features Implemented

- Create order with multiple garments, auto-calculated total bill
- Unique order ID generated automatically (UUID)
- Order status tracking: RECEIVED → PROCESSING → READY → DELIVERED
- Update order status via API
- Filter orders by status, customer name, or phone number
- Dashboard: total orders, total revenue, orders per status
- Estimated delivery date (defaults to 3 days from order creation)
- PostgreSQL persistence with indexed queries for performance

---

## API Endpoints

### Create Order
POST /orders
Body:
{
  "customerName": "Priya Sharma",
  "phoneNumber": "9876543210",
  "garments": [
    { "garmentType": "Shirt", "quantity": 2, "pricePerItem": 50.0 },
    { "garmentType": "Saree", "quantity": 1, "pricePerItem": 150.0 }
  ]
}
Response: 201 Created with order details and totalBill

### Get All Orders
GET /orders
Response: 200 OK with list of all orders

### Update Order Status
PATCH /orders/{id}/status?newStatus=PROCESSING
Response: 200 OK with updated order

### Filter Orders
GET /orders/filter?status=RECEIVED
GET /orders/filter?customerName=Priya
GET /orders/filter?phoneNumber=9876543210
Response: 200 OK with filtered list

### Dashboard
GET /dashboard
Response:
{
  "totalOrders": 10,
  "totalRevenue": 2500.0,
  "ordersByStatus": {
    "RECEIVED": 3,
    "PROCESSING": 4,
    "READY": 2,
    "DELIVERED": 1
  }
}

---

## Tradeoffs

### What I skipped
- Authentication (no login/JWT) — out of scope for 72hr assignment
- Frontend UI — focused on clean backend APIs
- Input validation (@Valid) — would add with more time
- Unit tests — would add service layer tests

### What I would improve with more time
- Online payment gateway integration (Razorpay/Stripe)
- SMS/email notifications for customers when order status changes
- Special instructions field so customers can give care notes 
  to the laundry owner (e.g. "handle with care", "no bleach")
- Role-based access: owner dashboard vs customer view
- Search by garment type
- Deploy on Railway or Render for live access

---

## Project Structure

src/main/java/com/laundry/
├── controller/
│   ├── OrderController.java
│   └── DashboardController.java
├── service/
│   └── OrderService.java
├── repository/
│   └── OrderRepository.java
├── entity/
│   ├── Order.java
│   └── GarmentItem.java
├── enums/
│   └── OrderStatus.java
├── dto/
│   ├── OrderRequest.java
│   └── OrderResponse.java
└── LaundryManagementApplication.java