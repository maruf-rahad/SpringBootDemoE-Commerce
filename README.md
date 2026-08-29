# 🛒 E-Commerce Backend REST API

A robust, production-grade E-Commerce Backend REST API built with **Spring Boot 3**, **Spring Security**, **JWT (JSON Web Tokens)**, and **Spring Data JPA**.

This application provides a complete e-commerce solution including user authentication, role-based access control, product catalog management, product reviews & ratings, interactive shopping cart, order processing with status tracking workflows, user profile management, and global exception handling.

---

## 🛠️ Tech Stack & Technologies

* **Java:** 17+
* **Framework:** Spring Boot 3
* **Security:** Spring Security (Stateless JWT Authentication & BCrypt Password Encoding)
* **Database & ORM:** PostgreSQL / Spring Data JPA & Hibernate
* **Object Mapping:** ModelMapper
* **Utilities:** Lombok
* **Build Tool:** Maven

---

## ✨ Key Features

### 🔐 Authentication & Authorization
* **JWT Authentication:** Secure stateless authentication token generation and validation.
* **Role-Based Access Control (RBAC):** Distinct permissions for `ROLE_CUSTOMER` and `ROLE_ADMIN`.
* **BCrypt Password Hashing:** Strong password encryption using Spring Security's `BCryptPasswordEncoder`.

### 👤 User Profile & Account Management
* **Profile Lookup & Edit:** Update personal details, contact info, and shipping addresses.
* **Password Change:** Secure password changes verifying existing passwords before updates.

### 📦 Product & Review Catalog
* **Product Management:** Full CRUD operations for products (Admin restricted for write operations).
* **Reviews & Ratings:** Customers can submit ratings (1-5 stars) and detailed reviews for products.

### 🛒 Shopping Cart & Orders
* **Persistent Shopping Cart:** Add, update, remove items, and calculate totals dynamically per user.
* **Order Processing:** Convert active cart items into finalized customer orders.
* **Order Tracking & Status Workflow:** Comprehensive order tracking (`PLACED` → `PROCESSING` → `SHIPPED` → `DELIVERED` / `CANCELLED`) with strict state transition validation.
* **Order Cancellation:** Customers can cancel orders prior to shipment or processing.

### 🛡️ System Resilience & Error Handling
* **Global Exception Handling:** `@ControllerAdvice` handling `ResourceNotFoundException`, `BadRequestException`, and runtime errors with standardized JSON error responses.
* **Data DTO Layering:** Decoupled Entity and DTO representations using `ModelMapper` to prevent internal data exposure.

---

## 📡 API Reference & Endpoints

### 🔐 Authentication & Public Routes
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/register` | Public | Register a new customer account |
| `POST` | `/login` | Public | Authenticate user & receive JWT token |

### 👤 User Profile Management
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/user/profile` | Authenticated | Retrieve logged-in user profile details |
| `PUT` | `/user/profile` | Authenticated | Update user name, phone, email, address |
| `PUT` | `/user/change-password` | Authenticated | Change user account password |

### 📦 Products & Reviews
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/product` | Public | Retrieve all products |
| `GET` | `/product/{id}` | Public | Get product details by ID |
| `POST` | `/product` | Admin | Create a new product |
| `PUT` | `/product/{id}` | Admin | Update existing product |
| `DELETE` | `/product/{id}` | Admin | Delete a product |
| `GET` | `/products/{productId}/reviews` | Public | Get reviews & ratings for a product |
| `POST` | `/products/{productId}/reviews` | Customer | Submit a product review/rating |

### 🛒 Shopping Cart
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/cart` | Customer/Admin | View active shopping cart |
| `POST` | `/cart/add` | Customer/Admin | Add product item to cart |
| `PUT` | `/cart/item/{itemId}` | Customer/Admin | Update cart item quantity |
| `DELETE` | `/cart/item/{itemId}` | Customer/Admin | Remove item from cart |

### 🚚 Orders & Order Tracking
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/orders` | Customer/Admin | Checkout cart and create order |
| `GET` | `/orders/{orderId}/track` | Customer/Admin | Track order status & lifecycle timestamps |
| `PUT` | `/orders/{orderId}/cancel` | Customer | Cancel an order prior to shipping |
| `PUT` | `/orders/admin/{orderId}/status`| Admin | Update order status (`PROCESSING`, `SHIPPED`, `DELIVERED`) |

### 👑 Admin Management
| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/admin/create-admin` | Admin | Register a new admin account |
| `GET` | `/admin/all-users` | Admin | Retrieve list of all registered users |

---

## ⚡ Getting Started

### Prerequisites
* **Java JDK:** 17 or higher
* **Maven:** 3.8+
* **Database:** PostgreSQL installed and running on port `5432`

### Application Configuration (`src/main/resources/application.properties`)
```properties
spring.application.name=demomaven

spring.datasource.url=jdbc:postgresql://localhost:5432/Ecommercedemo
spring.datasource.username=postgres
spring.datasource.password=maruf

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update