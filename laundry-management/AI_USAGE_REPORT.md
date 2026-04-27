# AI Usage Report

## Tools Used
- ChatGPT (GPT-4)
- Claude.ai (claude.ai)
- Antigravity Editor (Claude Sonnet)

---

## Prompt Log

### 1. Entity Design
**Prompt:**
"I'm building a laundry order management system using
Spring Boot 3, Spring Data JPA, PostgreSQL, and Lombok.

Create two classes:
1. Order.java — JPA entity with id (UUID), customerName, phoneNumber,
status (Enum: RECEIVED, PROCESSING, READY, DELIVERED),
totalBill, createdAt (@PrePersist), estimatedDelivery,
garments (List<GarmentItem> as @ElementCollection)
Table name: laundry_orders with @Index on status and phoneNumber

2. GarmentItem.java — @Embeddable with garmentType, quantity, pricePerItem

3. OrderStatus.java — Enum with RECEIVED, PROCESSING, READY, DELIVERED

Rules: PostgreSQL compatible, use Lombok @Data @Builder
@NoArgsConstructor @AllArgsConstructor, no extra complexity"

**What AI gave:** Complete entity with all annotations and correct structure
**What I fixed:** 
- Changed UUID type to String for id (PostgreSQL compatibility)
- Removed duplicate garmentType field from Order entity
- Verified @PrePersist sets createdAt correctly
**What I learned:** @ElementCollection stores list in separate table,
@Table name needed because 'order' is reserved keyword in PostgreSQL

---

### 2. Entity Structure Review
**Prompt:**
I'm building a laundry order management system using
Spring Boot 3, Spring Data JPA, PostgreSQL, and Lombok.

The system needs to:
- Create orders with garment items
- Track and update order status
- Filter orders by status, customer name, phone number
- Show dashboard: total orders, total revenue, orders per status
- Support estimated delivery date (optional field)

Create two classes:

1. Order.java — JPA entity with:
   - id: UUID, auto-generated using @GeneratedValue(strategy = GenerationType.UUID)
   - customerName: String
   - phoneNumber: String
   - status: Enum (RECEIVED, PROCESSING, READY, DELIVERED), stored as String in DB
   - totalBill: Double
   - createdAt: LocalDateTime, auto-set before persist using @PrePersist
   - estimatedDelivery: LocalDateTime (nullable)
   - garments: List<GarmentItem>, stored as @ElementCollection in separate table

   Annotations: @Entity, @Data, @Builder, @NoArgsConstructor, @AllArgsConstructor
   Table: @Table(name="laundry_orders") with @Index on status and phoneNumber

2. GarmentItem.java — Embeddable class with:
   - garmentType: String
   - quantity: Integer
   - pricePerItem: Double

   Annotations: @Embeddable, @Data, @NoArgsConstructor, @AllArgsConstructor

3. OrderStatus.java — Enum with values:
   RECEIVED, PROCESSING, READY, DELIVERED

Rules:
- No extra fields or methods beyond what's listed
- No MapStruct, no unnecessary interfaces
- PostgreSQL compatible only
- @PrePersist to auto-set createdAt
- Keep it production-clean and readable

1st analyze pdf wisely...

**What AI gave:** Gap analysis showing missing estimatedDelivery field
and missing @Index for filter performance
**What I fixed:** Added estimatedDelivery field and @Index annotations
**What I learned:** Always map requirements to entity fields before coding

---

### 3. Repository Layer
**Prompt:**
"I'm building a laundry order management system using
Spring Boot 3, Spring Data JPA, and PostgreSQL.

Create OrderRepository.java interface that:
- Extends JpaRepository<Order, String>
- Package: com.laundry.repository
Add these query methods:
1. Find all orders by status (OrderStatus enum)
2. Find all orders by customerName (case-insensitive, partial match)
3. Find all orders by phoneNumber (exact match)
4. Find orders by status AND customerName together
5. Dashboard query: count orders grouped by status
6. Dashboard query: sum of totalBill (total revenue)"

**What AI gave:** Clean repository with all 6 methods using
Spring Data JPA naming and JPQL @Query
**What I fixed/verified:** Nothing — code was correct
**What I learned:** @Query JPQL for aggregate functions like
count grouped by field and sum

---

### 4. DTO Classes
**Prompt:** I'm building a laundry order management system using
Spring Boot 3 and Lombok.

Create two DTO classes in package com.laundry.dto:

1. OrderRequest.java — data user sends when creating an order:
   - customerName: String
   - phoneNumber: String
   - garments: List<GarmentItem> (import from com.laundry.entity)
   - estimatedDelivery: LocalDateTime (nullable, optional)

   Annotations: @Data, @NoArgsConstructor, @AllArgsConstructor, @Builder

2. OrderResponse.java — data API returns after create/fetch:
   - id: String
   - customerName: String
   - phoneNumber: String
   - status: OrderStatus (import from com.laundry.enums)
   - totalBill: Double
   - createdAt: LocalDateTime
   - estimatedDelivery: LocalDateTime
   - garments: List<GarmentItem>

   Annotations: @Data, @NoArgsConstructor, @AllArgsConstructor, @Builder

Rules:
- No validation annotations yet
- No MapStruct
- Keep it simple

**What AI gave:**  Generated two DTO classes — OrderRequest (customerName, phoneNumber, garments) and OrderResponse (id, customerName, phoneNumber, status, totalBill, createdAt, estimatedDelivery, garments) with proper structure and fields.
**What I fixed:** Nothing — code was correct

---

### 5. Service Layer
**Prompt:**  I'm building a laundry order management system using
Spring Boot 3, Spring Data JPA, PostgreSQL, and Lombok.

Create OrderService.java in package com.laundry.service.

Inject: OrderRepository (constructor injection, not @Autowired)

Create these methods:

1. createOrder(OrderRequest request) → OrderResponse
   - Map OrderRequest to Order entity
   - Calculate totalBill: sum of (quantity * pricePerItem) 
     for each GarmentItem in request
   - Set initial status to OrderStatus.RECEIVED
   - Set estimatedDelivery to 3 days from now if not provided
   - Save to DB using repository
   - Map saved Order to OrderResponse and return

2. updateOrderStatus(String orderId, OrderStatus newStatus) → OrderResponse
   - Find order by id, throw RuntimeException 
     with message "Order not found" if missing
   - Update status
   - Save and return as OrderResponse

3. getAllOrders() → List<OrderResponse>
   - Return all orders mapped to OrderResponse

4. filterOrders(OrderStatus status, String customerName, 
   String phoneNumber) → List<OrderResponse>
   - If status provided → filter by status
   - If customerName provided → filter by customerName
   - If phoneNumber provided → filter by phoneNumber
   - If status + customerName both provided → use combined query
   - All params are optional (nullable)

5. getDashboard() → Map<String, Object>
   - totalOrders: count of all orders
   - totalRevenue: sum of all totalBill
   - ordersByStatus: count per status

Rules:
- Use @Service annotation
- Private helper method mapToResponse() to convert 
  Order → OrderResponse (reuse across methods)
- No static methods
- No MapStruct
- Simple, readable code only

**What AI gave:** Complete service with all 5 methods,
null safety helpers, clean mapToResponse utility
**What I fixed:** Nothing — code was production quality
**What I learned:** Constructor injection over @Autowired,
stream().toList() in Java 16+, handling combined filters

---

### 6. Controller Layer
**Prompt:**
"Create OrderController.java in package com.laundry.controller
with 5 endpoints: POST /orders (201), GET /orders (200),
PATCH /orders/{id}/status with @RequestParam newStatus,
GET /orders/filter with optional status/customerName/phoneNumber,
GET /dashboard. Use @RequiredArgsConstructor, proper HTTP codes,
no try-catch, no MapStruct."

**What AI gave:** Two separate controllers - OrderController
and DashboardController, clean code with correct annotations
**What I fixed:** Nothing — structure was correct
**What I learned:** Separating dashboard into its own controller
keeps OrderController focused on order operations only

---

### 7. Frontend UI
**Prompt:**
I'm building a frontend for a Laundry Order Management System.
Backend is Spring Boot running on http://localhost:8080.

Build a single HTML file (no React, no build tools) with:

1. Dashboard Tab
   - 3 stat cards: Total Orders, Total Revenue, Active Orders
   - Orders breakdown by status (RECEIVED, PROCESSING, READY, DELIVERED)
   - Fetch from GET http://localhost:8080/dashboard

2. Create Order Tab
   - Form: Customer Name, Phone Number
   - Add multiple garments dynamically (garmentType, quantity, pricePerItem)
   - Submit to POST http://localhost:8080/orders
   - Show success message with Order ID and total bill

3. All Orders Tab
   - Table showing all orders with status badges
   - Filter by status dropdown, search by customer name
   - Fetch from GET http://localhost:8080/orders
   - Filter calls GET http://localhost:8080/orders/filter

4. Update Status Tab
   - Input: Order ID
   - Dropdown: RECEIVED, PROCESSING, READY, DELIVERED
   - Submit to PATCH http://localhost:8080/orders/{id}/status?newStatus=X
   - Show updated order details after success

Design requirements:
- Dark theme (#0e0e0e background)
- Clean, minimal, professional
- No external CSS frameworks
- All in one HTML file with embedded CSS and JS
- Use fetch() for all API calls
- Handle loading states and errors
- Status badges with different colors per status
- Mobile responsive

Rules:
- Single file only
- No jQuery, no Bootstrap
- Vanilla JS only
- All API calls must handle errors gracefully
- Show user-friendly error messages

**What AI gave:** Complete working single HTML file with
all 4 tabs, fetch() API integration, status badges
**What I fixed:** Nothing — worked on first try
**What I learned:** Single file HTML with embedded CSS/JS
is fastest way to ship a working frontend quickly

---

## Where AI Helped Most
- Scaffolding boilerplate saved ~2 hours
- Catching missing fields via requirement analysis

## Where AI Failed / I Had to Fix
- UUID type incompatibility with PostgreSQL
- Duplicate field in entity
- Missing input validation

## My Judgment Calls
- Used String id instead of UUID for PostgreSQL safety
- Added @Index for filter performance
- Structured packages manually for clarity