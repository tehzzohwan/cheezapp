# Cheezapp 🧀

A modern **multi-tenant appointment booking system** built with Spring Boot, designed for service providers (barbers, makeup artists, salons, consultants, etc.).

---

## ✨ Features

- **Multi-Tenancy Support** (Schema-based)
- **JWT Authentication** (User & Service Provider roles)
- **Role-Based Access Control**
- **Appointment Booking System** with conflict detection
- **Availability Slot Management**
- **Service Provider Dashboard**
- **User Profile & Booking History**
- **Swagger UI Documentation**
- **PostgreSQL + Liquibase / JPA Support**

---

## 🛠 Tech Stack

- **Backend**: Spring Boot 3.3.4 + Java 21
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT
- **ORM**: Spring Data JPA + Hibernate
- **API Documentation**: Springdoc OpenAPI (Swagger)
- **Build Tool**: Maven

---

## 🚀 Quick Start

### Prerequisites
- Java 21
- PostgreSQL (running on port 5432)
- Maven

### 1. Clone / Setup Project
```bash
cd cheezapp
```

### 2. Database Setup

Run the following SQL commands in PostgreSQL:

```sql
CREATE DATABASE cheezapp;
```

### 4. Run the Application
Bash./mvnw spring-boot:run
Or simply run the CheezappApplication.java class from IntelliJ IDEA.


### 5. Access URLs

Swagger UI: http://localhost:8080/swagger-ui.html
Base API URL: http://localhost:8080


## 🧪 Default Test Credentials
```
Email: bobby@yenosoft.com
Password: password
```

## 📌 Main API Endpoints
### Authentication

```
POST /api/auth/register — Register as normal user
POST /api/auth/service-provider/register — Register as Service Provider
POST /api/auth/login — Login (both roles)
```

### Appointments
```
GET /api/appointments/available-slots?serviceProviderId=1&date=2026-04-05 — Get available slots
POST /api/appointments/book/{slotId} — Book an appointment
GET /api/appointments/my-bookings — View my bookings
PUT /api/appointments/cancel/{appointmentId} — Cancel appointment
```
### Service Providers
```
GET /api/service-providers — List all service providers
GET /api/appointments/my-slots — View my availability slots (for Service Providers)
```
### Profile
```
GET /api/profile/me — Get my profile
PUT /api/profile/me — Update my profile
```

## 🏗 Project Structure
```
Bashsrc/main/java/com/yenosoft/cheezapp/
├── config/
├── controller/
├── domain/
├── dto/
├── exception/
├── repository/
├── security/
├── service/
└── CheezappApplication.java
```

### 📋 Business Rules (for now)

Business hours: 9:00 AM – 8:00 PM
Appointment duration: Minimum 1 hour, Maximum 2 hours
Slots can only be created for future dates
Only normal users (ROLE_USER) can book appointments


### 👨‍💻 Author
```
Bobby Enomate
Yenosoft
```
