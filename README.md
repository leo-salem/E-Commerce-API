#  E-Commerce API

A fully functional, production-ready E-Commerce REST API built with Spring Boot, featuring JWT authentication, Redis-based token blacklisting, modular architecture (Controller, Service, Repository), The project is powerful **Backend API** that uses DTOs for request/response abstraction, input validation, and role-based access control.

---

##  Tech Stack

| Layer         | Technology                                  |
|---------------|---------------------------------------------|
| Language      | Java 21                                     |
| Framework     | Spring Boot                                 |
| ORM           | Hibernate / JPA                             |
| Security      | Spring Security + JWT                       |
| Token Store   | Redis                                       |
| DB            | MySQL / PostgreSQL                          |
| Testing       | Postman                         |
| Build Tool    | Maven                                       |
| Docs          | Swagger / SpringDoc                         |
| Validation    | Javax Validation API                        |

---

##  Database Design

### ERD
<img width="1753" height="1361" alt="E-COMMERCY" src="https://github.com/user-attachments/assets/703a1eae-556a-45ce-a073-68164226601e" />

### DB Schemas
<img width="1268" height="831" alt="Screenshot 2025-08-12 065916" src="https://github.com/user-attachments/assets/bd974bf2-1b8c-4405-af6b-13c24975aba7" />

##  Project Structure
```bash
E-COMMERCY/
├── controller/ # REST Controllers (API endpoints)
├── dto/ # Data Transfer Objects(Request DTOs,Response DTOs,DTO <->Mapper <-> Entity ,Slim DTOs)
├── exception/ # Custom exceptions & global handler
├── model/ # JPA entities
├── repository/ # Spring Data JPA repositories
├── security/ ## Security & configurations, JWT, Redis, and authentication filters
├── service/ # Business logic services
│ ├── impl/ # Service implementations
```

---

## Strength Points

- **Modular and Layered Architecture** for easy scaling.
- **Feature-based Packaging** (service/product, service/cart, etc.) for better maintainability.
- **DTO-based API Layer** for strict data validation and clean responses.
- **Custom Exception Handling** with unified error responses.
- **JWT + Redis** for secure, logout-safe authentication.
- **Manual Deletion Handling** for critical operations.
- **Fully Tested with Postman** for all API endpoints.
- **Ready for Production** with logging, validation, and role-based access.

---

## Security Features
- **JWT Authentication & Authorization**
- **Token Blacklisting** (logout-safe)
- **High-performance token tracking** using Redis
- Ready for caching use cas
---

## Roles & Capabilities

| Role   | Capabilities                                     |
|--------|--------------------------------------------------|
| User   | Browse, order, Get Product list, update profile  |
| Admin  | Manage products, users, categories, orders       |

---

## Controllers & EndPoints

<img width="1243" height="833" alt="Screenshot 2025-08-12 075003" src="https://github.com/user-attachments/assets/3b73d843-0a29-405d-a091-573dbf105edd" />

<img width="903" height="782" alt="Screenshot 2025-08-12 075027" src="https://github.com/user-attachments/assets/18e50acc-a5b1-4a9f-bcf8-95902ef86664" />


---
## Installation & Run

```bash
# Clone the repository
git clone https://github.com/leo-salem/E-Commerce-API.git

# Navigate into the project
cd E-COMMERCY

# Configure application.properties with your MySQL & Redis settings

# Build and run
mvn spring-boot:run


