# User Authentication & Authorization Microservice

This microservice is responsible for **user management**, including registration, login, and role-based authorization. It is part of a larger **e-commerce microservices ecosystem**, built with **Java**, **Spring Boot**, **Spring Security**, and **MySQL**.

## 🔐 Key Features

- User **registration** and **login** endpoints
- **JWT-based authentication**
- **Role-based access control** using `@PreAuthorize` (Admin / User)
- **Account activation/deactivation** functionality
- **Role updates** and **user deletion** by Admins
- Secure **check-role** endpoint to validate roles via token
- CORS configuration to enable frontend communication (e.g., Angular)

## ⚙️ Technologies Used

- **Java 17**
- **Spring Boot 3**
- **Spring Security + JWT**
- **Spring Data JPA**
- **MySQL**
- **Lombok**
- **Validation with Jakarta**
- **Dockerized** (ready for containerized deployment)

## 📁 Project Structure

```
src/
├── controller/      # AuthController, UserController
├── model/           # Entity, Enum, DTOs
├── repository/      # UserRepository
├── service/         # AuthService, UserService
├── config/          # Security, JWT filters
```

## 🔌 API Endpoints

- `POST /auth/register` – Register new user  
- `POST /auth/login` – Authenticate and return JWT  
- `GET /users` – List all users (Admin only)  
- `PUT /{userId}/role` – Update user role  
- `PUT /{userId}/activate` – Activate user  
- `PUT /{userId}/deactivate` – Deactivate user  
- `DELETE /{userId}/delete` – Delete user  
- `GET /check-role` – Check if user has specific role (via token)

## 🚀 Deployment

✅ Successfully deployed on **[Railway](https://railway.app/)** for seamless integration with other services in the system.

## 🧪 Testing

Basic unit testing is in progress. The project is structured to support testing via **JUnit 5** and **Mockito**.

## 🧠 Notes

- Uses **ResponseEntity** to wrap all responses with status codes.
- Utilizes **DTOs** to separate internal logic from exposed APIs.
- JWT is sent via `Authorization` header (`Bearer <token>`).

