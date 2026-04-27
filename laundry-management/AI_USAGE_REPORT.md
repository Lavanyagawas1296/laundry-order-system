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
"Analyze this assignment PDF and check if my Order entity
structure covers all required features:
- Create order, track status, calculate billing,
  filter orders, dashboard data, estimated delivery date"

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
**Prompt:** I need two DTO classes for my laundry order API:

1. OrderRequest - data user sends when creating order
2. OrderResponse - data API returns after creating/fetching order

**What AI gave:**  Generated two DTO classes — OrderRequest (customerName, phoneNumber, garments) and OrderResponse (id, customerName, phoneNumber, status, totalBill, createdAt, estimatedDelivery, garments) with proper structure and fields.
**What I fixed:** Nothing — code was correct

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