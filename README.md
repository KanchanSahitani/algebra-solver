# Algebra Solver — Spring Boot Application

A RESTful Spring Boot application for **storing and evaluating algebraic equations** using an **expression (postfix) tree**.

## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---

## Table of Contents

1. [Overview](#overview)
2. [Architecture & Design](#architecture--design)
3. [Swagger / OpenAPI Documentation](#swagger--openapi-documentation)
4. [Prerequisites](#prerequisites)
5. [Setup & Run](#setup--run)
6. [API Endpoints](#api-endpoints)
7. [Testing](#testing)
8. [Deployment Guide (Live URL)](#deployment-guide-live-url)
9. [Loom Video Demo](#loom-video-demo)
10. [Project Structure](#project-structure)

## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---

## Swagger / OpenAPI Documentation

Interactive Swagger UI is enabled by default:
- **Local Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---

## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---

## Overview

This application:

- **Parses** algebraic equations (infix notation) into postfix notation using the **Shunting-Yard algorithm**.
- **Stores** each equation in an **expression tree** (postfix tree) where operators are parent nodes and operands are child nodes.
- **Retrieves** stored equations by traversing the tree and reconstructing the infix representation.
- **Evaluates** equations by substituting variable values and computing the result via tree traversal.

### Supported Features

| Feature | Example |
|## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---|## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---|
| Implicit multiplication | `3x` → `3 * x` |
| Exponentiation | `x^2 + y^2` |
| Parenthesised sub-expressions | `(x + 1) * (y - 2)` |
| Unary minus | `-x + 3` |
| Decimal coefficients | `3.5x + 2.1` |
| Multi-variable equations | `3x + 2y - z` |

## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---

## Architecture & Design

```
Controller (REST) → Service (Business Logic) → Repository (In-Memory Storage)
                       ↕
              ExpressionParser    ← Tokenizer + Shunting-Yard + Tree Builder
              ExpressionEvaluator ← Recursive tree evaluation
```

**Key design patterns used:**

- **Layered Architecture** — Controller → Service → Repository separation
- **Dependency Injection** — Constructor-based injection via Spring
- **DTO Pattern** — Separate request/response DTOs from domain models
- **Global Exception Handler** — `@RestControllerAdvice` for consistent error responses
- **Repository Pattern** — In-memory storage with thread-safe `ConcurrentHashMap`

## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---

## Prerequisites

- **Java 17+** (tested with Java 22)
- **Maven 3.8+** (or use the included Maven Wrapper `mvnw.cmd`)

## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---

## Setup & Run

### 1. Clone / Navigate to the project

```bash
cd algebra-solver
```

### 2. Build the project

```bash
# Using Maven Wrapper (recommended)
mvnw.cmd clean package

# Or using system Maven
mvn clean package
```

### 3. Run the application

```bash
# Using Maven
mvnw.cmd spring-boot:run

# Or using the built JAR
java -jar target/algebra-solver-1.0.0.jar
```

The application starts on **http://localhost:8080**.

## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---

## API Endpoints

### 1. Store an Equation

```
POST /api/equations/store
Content-Type: application/json
```

**Request Body:**
```json
{
  "equation": "3x + 2y - z"
}
```

**Response (201 Created):**
```json
{
  "message": "Equation stored successfully",
  "equationId": "1"
}
```

## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---

### 2. Retrieve All Stored Equations

```
GET /api/equations
```

**Response (200 OK):**
```json
{
  "equations": [
    {
      "equationId": "1",
      "equation": "3x + 2y - z"
    },
    {
      "equationId": "2",
      "equation": "x^2 + y^2 - 4"
    }
  ]
}
```

## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---

### 3. Evaluate an Equation

```
POST /api/equations/{equationId}/evaluate
Content-Type: application/json
```

**Request Body:**
```json
{
  "variables": {
    "x": 2,
    "y": 3,
    "z": 1
  }
}
```

**Response (200 OK):**
```json
{
  "equationId": "1",
  "equation": "3x + 2y - z",
  "variables": {
    "x": 2.0,
    "y": 3.0,
    "z": 1.0
  },
  "result": 11.0
}
```

> **Note:** `3x + 2y - z` with `x=2, y=3, z=1` → `3(2) + 2(3) - 1 = 6 + 6 - 1 = 11`

## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---

### Error Responses

All errors return a consistent JSON structure:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Descriptive error message"
}
```

| Scenario | Status | Message |
|## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---|## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---|## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---|
| Blank/missing equation | 400 | `equation: must not be blank` |
| Invalid syntax | 400 | `Mismatched parentheses` |
| Equation not found | 400 | `Equation not found with id: 999` |
| Missing variable | 400 | `Missing value for variable: 'y'` |
| Division by zero | 400 | `Division by zero` |

## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---

## Testing

### Run all tests

```bash
mvnw.cmd test
```

### Run with coverage report (JaCoCo)

```bash
mvnw.cmd test
# Report generated at: target/site/jacoco/index.html
```

### Test categories

| Test Class | Scope | Tests |
|## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---|## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---|## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---|
| `ExpressionParserTest` | Tokenizer, Shunting-Yard, tree building, infix reconstruction | 13 |
| `ExpressionEvaluatorTest` | Tree evaluation, edge cases (division by zero, missing vars, unary minus) | 10 |
| `EquationServiceTest` | Business-logic integration (store, retrieve, evaluate) | 7 |
| `EquationRepositoryTest` | In-memory repository (save, find, clear) | 4 |
| `EquationControllerTest` | Full Spring MVC integration (REST endpoints, validation, error handling) | 9 |

### Test with Postman

Import the following requests into Postman:

1. **Store** — `POST http://localhost:8080/api/equations/store`  
   Body: `{ "equation": "3x + 2y - z" }`

2. **List** — `GET http://localhost:8080/api/equations`

3. **Evaluate** — `POST http://localhost:8080/api/equations/1/evaluate`  
   Body: `{ "variables": { "x": 2, "y": 3, "z": 1 } }`

## 🌐 Live Deployment
- **Live Base URL**: https://algebra-solver-1bm3.onrender.com
- **Swagger Documentation**: https://algebra-solver-1bm3.onrender.com/swagger-ui.html

---

## Project Structure

```
algebra-solver/
├── pom.xml
├── mvnw.cmd
├── README.md
├── .mvn/wrapper/
│   └── maven-wrapper.properties
└── src/
    ├── main/java/com/algebra/
    │   ├── AlgebraSolverApplication.java
    │   ├── controller/
    │   │   └── EquationController.java
    │   ├── dto/
    │   │   ├── StoreEquationRequest.java
    │   │   ├── StoreEquationResponse.java
    │   │   ├── EvaluateRequest.java
    │   │   ├── EvaluateResponse.java
    │   │   ├── EquationDto.java
    │   │   └── EquationListResponse.java
    │   ├── exception/
    │   │   └── GlobalExceptionHandler.java
    │   ├── model/
    │   │   ├── Equation.java
    │   │   └── TreeNode.java
    │   ├── repository/
    │   │   └── EquationRepository.java
    │   └── service/
    │       ├── EquationService.java
    │       ├── ExpressionEvaluator.java
    │       └── ExpressionParser.java
    ├── main/resources/
    │   └── application.properties
    └── test/java/com/algebra/
        ├── controller/
        │   └── EquationControllerTest.java
        ├── repository/
        │   └── EquationRepositoryTest.java
        └── service/
            ├── EquationServiceTest.java
            ├── ExpressionEvaluatorTest.java
            └── ExpressionParserTest.java
```
