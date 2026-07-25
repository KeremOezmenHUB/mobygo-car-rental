# MobyGo — Car Rental Web Application

> **FHNW Internet Technology — Group Project**  
> A responsive web application for browsing rental cars, comparing categories and prices, and creating car bookings across several Swiss rental locations.

---

## Group Composition

| Name | Main Contribution |
|------|-------------------|
| **Ifin Anwar** | Backend development, JPA domain model, rental business logic, price calculation, double-booking validation, Spring Security setup |
| **Bajram** | Frontend implementation, responsive UI design, JavaScript integration with REST API, booking flow, admin dashboard screens |
| **Kerem** | REST API design, OpenAPI/Swagger documentation, seed data, Codespaces/local setup, documentation structure and testing of user flows |

---

## Links

| Resource | URL |
|----------|-----|
| 🎥 **Video Presentation** | [MobyGo video presentation] |
| 🧩 **Frontend Approach** | Pro-code frontend with HTML, CSS and JavaScript included in the repository |
| 📄 **OpenAPI / Swagger Documentation** | `/swagger-ui.html` — available at `http://localhost:8080/swagger-ui.html` when running locally, or at the forwarded Codespaces URL after starting the app |
| 💻 **GitHub Repository** | [mobygo-car-rental](https://github.com/KeremOezmenHUB/mobygo-car-rental) |

> **No public deployment is required for the assessment.** The project is intended to run locally or in GitHub Codespaces. The backend exposes a REST API that is consumed by the included pro-code frontend implemented with HTML, CSS and JavaScript.

---

## Table of Contents

- [1. Analysis](#1-analysis)
  - [1.1 Scenario & Domain](#11-scenario--domain)
  - [1.2 Actors](#12-actors)
  - [1.3 Use Cases](#13-use-cases)
  - [1.4 User Stories](#14-user-stories)
- [2. Domain Design](#2-domain-design)
  - [2.1 Domain Model](#21-domain-model)
  - [2.2 Database Schema](#22-database-schema)
  - [2.3 Seed Data](#23-seed-data)
- [3. Frontend Implementation](#3-frontend-implementation)
  - [3.1 Technology Choice](#31-technology-choice)
  - [3.2 Design System](#32-design-system)
  - [3.3 Views](#33-views)
- [4. Business Logic & API Design](#4-business-logic--api-design)
  - [4.1 Business Rules](#41-business-rules)
  - [4.2 REST API](#42-rest-api)
- [5. Data & API Implementation](#5-data--api-implementation)
  - [5.1 Architecture](#51-architecture)
  - [5.2 Backend Technology Stack](#52-backend-technology-stack)
  - [5.3 Package Structure](#53-package-structure)
  - [5.4 Design Patterns & Principles](#54-design-patterns--principles)
- [6. Security](#6-security)
- [7. Demonstrator — Installation & Running](#7-demonstrator--installation--running)
  - [7.1 Running Locally](#71-running-locally)
  - [7.2 Running on GitHub Codespaces](#72-running-on-github-codespaces)
  - [7.3 Example API Request](#73-example-api-request)
- [8. Project Management](#8-project-management)
  - [8.1 Milestones](#81-milestones)
  - [8.2 Requirements Coverage](#82-requirements-coverage)
- [9. Default Credentials](#9-default-credentials)

---

# 1. Analysis

## 1.1 Scenario & Domain

**MobyGo** is a car rental web application for customers who want to quickly find and book a vehicle in Switzerland. The application shows available cars, their categories, daily prices, and rental locations. Customers can create a booking by choosing a car, pickup location, dropoff location, and rental period.

The business scenario is based on a small car rental company with several branches. The company needs a digital solution where public users can browse the fleet and create rental requests, while administrators can maintain business data such as cars, locations, rentals, and users.

A typical customer journey looks like this:

1. The user opens the web application on a desktop or mobile device.
2. The user browses the available cars and can filter them by category or location.
3. The user checks the daily rates for each car category.
4. The user opens the booking page and enters customer details, selected car, pickup and dropoff location, start date, and end date.
5. The system validates the booking, checks that the car is not already rented in the selected period, calculates the total price, and stores the rental.
6. An administrator can log in and manage the fleet, locations, rentals, and users.

## 1.2 Actors

| Actor | Role |
|-------|------|
| **Public User / Customer** | Browses cars and locations, checks prices, and creates a guest booking without an account. |
| **Registered User** | Can be linked to bookings and can access user-specific rental data. |
| **Administrator** | Logs in with Basic Auth and manages cars, locations, rentals, and users through protected API endpoints and the admin interface. |

## 1.3 Use Cases

### Public User / Customer

- **Browse Cars** — See all available vehicles with brand, model, category, license plate, and location.
- **Filter Cars** — Filter cars by category or rental location.
- **Browse Locations** — See all MobyGo rental stations in Switzerland.
- **Check Daily Rates** — View the daily price per car category.
- **Book a Car** — Create a rental by selecting a car, rental dates, pickup/dropoff location, and customer contact details.

### Administrator

- **Authenticate** — Use HTTP Basic Auth to access protected functionality.
- **Manage Cars** — Create, list, update, and delete vehicles in the fleet.
- **Manage Locations** — Create, list, update, and delete rental stations.
- **Manage Rentals** — View all existing bookings.
- **Manage Users** — Create, list, and delete registered users.
- **Use API Documentation** — Explore and test endpoints through Swagger UI.

## 1.4 User Stories

The generic assessment user stories are mapped to the MobyGo implementation as follows:

| Generic Story | Implemented As |
|---------------|----------------|
| 1. Admin — Web app on mobile and desktop | Responsive Bootstrap-based frontend with navigation, cards, tables, and forms. |
| 2. Admin — Consistent visual appearance | Shared CSS file (`/css/style.css`), consistent navbar, buttons, cards, badges, and typography across pages. |
| 3. Admin — List views for business data | Admin dashboard contains list/table views for cars, locations, rentals, and users. |
| 4. Admin — Edit and create views | Admin dashboard provides create forms for cars and locations; REST API supports update and delete endpoints. |
| 5. Admin — Log in to authenticate | Spring Security with HTTP Basic Authentication and seeded admin user. |
| 6. User — List views for public pages | Public car overview, location overview, category filter, rates bar, and booking page. |
| 7. Optional User Authentication | Registered users exist in the data model; guest booking is also supported for a lower-friction customer journey. |

### Extended User Stories

| # | User Story |
|---|------------|
| US-A1 | As an admin, I want to log in so that only authorized people can manage fleet data. |
| US-A2 | As an admin, I want to see all cars in a table so that I can manage the fleet efficiently. |
| US-A3 | As an admin, I want to create and update cars so that new vehicles can be added to the platform. |
| US-A4 | As an admin, I want to manage rental locations so that customers can choose correct pickup and dropoff stations. |
| US-A5 | As an admin, I want to view all rentals so that I can monitor current and future bookings. |
| US-U1 | As a customer, I want to browse available cars so that I can find a suitable vehicle. |
| US-U2 | As a customer, I want to filter cars by category or location so that I can narrow down my search. |
| US-U3 | As a customer, I want to see daily rates so that I can estimate the rental cost. |
| US-U4 | As a customer, I want to book a car online so that I can reserve it without calling the company. |
| US-U5 | As a customer, I want the application to work on my phone so that I can book a car while travelling. |

---

# 2. Domain Design

## 2.1 Domain Model

The MobyGo domain model is based on four main JPA entities and one enum. The entities represent the core concepts of the car rental business: users, cars, rental locations, and rental bookings.

```
┌────────────────┐ 1        * ┌────────────────┐
│      User      │────────────►│     Rental     │
│────────────────│             │────────────────│
│ id             │             │ id             │
│ username       │             │ user (FK)      │
│ password       │             │ customerName   │
│ role           │             │ customerEmail  │
└────────────────┘             │ car (FK)       │
                               │ pickupLocation │
                               │ dropoffLocation│
                               │ startDate      │
                               │ endDate        │
                               │ totalPrice     │
                               └───────▲────────┘
                                       │ *
                                       │
┌────────────────┐ 1        * ┌────────┴───────┐
│    Location    │────────────►│      Car       │
│────────────────│             │────────────────│
│ id             │             │ id             │
│ name           │             │ brand          │
│ city           │             │ model          │
│ address        │             │ licensePlate   │
└────────────────┘             │ category       │──► CarCategory enum
                               │ location (FK)  │    ECONOMY / COMPACT
                               └────────────────┘    SUV / LUXURY / VAN
```

**Main entities**

| Entity | Purpose |
|--------|---------|
| `Car` | Represents a rentable vehicle with brand, model, license plate, category, and location. |
| `Location` | Represents a MobyGo branch where cars can be picked up or returned. |
| `Rental` | Represents a booking with selected car, customer data, rental period, locations, and total price. |
| `User` | Represents registered users and admins used by Spring Security. |
| `CarCategory` | Enum defining car classes and their pricing categories. |

## 2.2 Database Schema

The application uses an **H2 in-memory relational database** (`jdbc:h2:mem:mobygo`). The schema is generated by Hibernate on application startup with `spring.jpa.hibernate.ddl-auto=create-drop`.

| Table | Purpose |
|-------|---------|
| `car` | Stores all vehicles in the fleet. |
| `location` | Stores all rental branches. |
| `rental` | Stores all bookings and calculated total prices. |
| `users` | Stores admin and registered user accounts. |

### Detailed Table Structure

**`car`**

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key, auto-generated |
| brand | VARCHAR | Car brand, e.g. VW or BMW |
| model | VARCHAR | Car model |
| license_plate | VARCHAR | Vehicle license plate |
| category | VARCHAR | Enum value: ECONOMY, COMPACT, SUV, LUXURY, VAN |
| location_id | BIGINT | Foreign key to `location` |

**`location`**

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key, auto-generated |
| name | VARCHAR | Name of the rental station |
| city | VARCHAR | City where the station is located |
| address | VARCHAR | Full address |

**`rental`**

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key, auto-generated |
| user_id | BIGINT | Optional foreign key to `users` |
| customer_name | VARCHAR | Guest customer name |
| customer_email | VARCHAR | Guest customer email |
| car_id | BIGINT | Foreign key to rented car |
| pickup_location_id | BIGINT | Foreign key to pickup location |
| dropoff_location_id | BIGINT | Foreign key to dropoff location |
| start_date | DATE | First rental day |
| end_date | DATE | Return date |
| total_price | DOUBLE | Calculated rental price |

**`users`**

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key, auto-generated |
| username | VARCHAR | Unique login name |
| password | VARCHAR | BCrypt-hashed password |
| role | VARCHAR | ADMIN or USER |

## 2.3 Seed Data

The class `DataInitializer` inserts demo data on startup:

- **3 locations**: Zurich Airport, Basel Central, Bern City
- **8 cars** across different categories: ECONOMY, COMPACT, SUV, LUXURY, VAN
- **3 users**: one admin and two standard users

This seed data makes the demonstrator usable immediately after starting the application.

---

# 3. Frontend Implementation

## 3.1 Technology Choice

The project contains a **static HTML/CSS/JavaScript frontend** served directly by Spring Boot from `src/main/resources/static`. The frontend consumes the REST API through JavaScript `fetch` calls.

The project uses a **pro-code frontend approach** instead of a low-code frontend. The frontend is implemented directly in the repository with HTML, CSS and JavaScript. This was chosen because the project requires custom UI behaviour, direct control over the user journey, and a reproducible demonstrator that runs together with the Spring Boot application.

This approach keeps the frontend, backend and demonstrator reproducible because the included frontend works immediately after starting the Spring Boot application locally or in GitHub Codespaces.

## 3.2 Design System

The visual design focuses on a clean mobility brand identity:

- Consistent **MobyGo** navigation bar across public and admin pages
- Responsive Bootstrap layout for desktop, tablet, and mobile
- Cards for car and location overviews
- Tables and tabs in the admin dashboard
- Category badges for different vehicle types
- Clear primary buttons for booking and management actions
- Shared styling in `src/main/resources/static/css/style.css`

## 3.3 Views

The project implements more than the four required views:

| View | File / Route | Type | Description |
|------|--------------|------|-------------|
| **Cars Overview** | `/` / `index.html` | Public list view | Shows available cars, rates, filters, and links to booking. |
| **Locations Overview** | `/locations.html` | Public list view | Shows all rental branches and links back to available cars. |
| **Booking View** | `/rentals.html` | Public create form | Lets a customer create a guest rental booking. |
| **Admin Dashboard** | `/admin.html` | Protected management view | Contains tabs for cars, locations, rentals, and users. |
| **Swagger UI** | `/swagger-ui.html` | API documentation view | Interactive documentation for all REST endpoints. |
| **H2 Console** | `/h2-console` | Developer view | Inspect database tables during demonstration. |

---

# 4. Business Logic & API Design

## 4.1 Business Rules

### Rule 1 — Price Calculation

The total rental price is calculated automatically in the service layer. The customer does not send the final price directly. Instead, the backend calculates it from the selected car category and the number of rental days.

| Category | Daily Rate (CHF) |
|----------|------------------|
| ECONOMY | 50 |
| COMPACT | 80 |
| SUV | 120 |
| LUXURY | 200 |
| VAN | 100 |

Formula:

```text
totalPrice = dailyRate × numberOfDays
numberOfDays = endDate - startDate
```

Example: An ECONOMY car from 1 July to 5 July is 4 days × CHF 50 = **CHF 200**.

### Rule 2 — No Double Booking

Before saving a rental, the backend checks whether the selected car already has another rental that overlaps with the requested date range. If a conflict exists, the booking is rejected with a clear error message.

### Rule 3 — Valid Rental Period

The backend validates the rental dates:

- Start date and end date are required.
- Start date must be before end date.
- Start date cannot be in the past.

### Rule 4 — Customer Identification

A rental can be created either for a registered user (`userId`) or as a guest booking with `customerName` and `customerEmail`. At least a customer name or a user reference is required.

## 4.2 REST API

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/` | Public | Welcome message |
| GET | `/api/rates` | Public | Daily rates per car category |
| GET | `/api/cars` | Public | List all cars |
| GET | `/api/cars?locationId=1` | Public | Filter cars by location |
| GET | `/api/cars?category=SUV` | Public | Filter cars by category |
| GET | `/api/cars/{id}` | Public | Get single car |
| POST | `/api/cars` | Auth required | Create car |
| PUT | `/api/cars/{id}` | Auth required | Update car |
| DELETE | `/api/cars/{id}` | Auth required | Delete car |
| GET | `/api/locations` | Public | List all rental locations |
| GET | `/api/locations/{id}` | Public | Get single location |
| POST | `/api/locations` | Auth required | Create location |
| PUT | `/api/locations/{id}` | Auth required | Update location |
| DELETE | `/api/locations/{id}` | Auth required | Delete location |
| POST | `/api/rentals` | Public | Create a rental booking |
| GET | `/api/rentals` | Auth required | List all rentals |
| GET | `/api/rentals/user/{username}` | Auth required | List rentals for a specific user |
| GET | `/api/users` | Auth required | List all users |
| POST | `/api/users` | Auth required | Create user |
| DELETE | `/api/users/{id}` | Auth required | Delete user |

The full interactive OpenAPI documentation is available at:

```text
/swagger-ui.html
```

---

# 5. Data & API Implementation

## 5.1 Architecture

The application follows a layered Spring Boot architecture with a frontend/backend separation over HTTP.

```
┌──────────────────────────────────────┐
│ Pro-code Frontend (HTML/CSS/JS)       │  Tier 1
│ HTML, CSS, JavaScript, REST client    │
└──────────────────┬───────────────────┘
                   │ REST / HTTP / JSON
┌──────────────────▼───────────────────┐
│ Controller Layer                      │  Tier 2 / Layer 1
│ CarController, LocationController,    │
│ RentalController, UserController      │
├──────────────────────────────────────┤
│ Service Layer                         │  Layer 2
│ CarService, LocationService,          │
│ RentalService, UserService            │
│ Business rules and validations        │
├──────────────────────────────────────┤
│ Repository Layer                      │  Layer 3
│ Spring Data JPA repositories          │
├──────────────────────────────────────┤
│ Database Layer                        │
│ H2 in-memory relational database      │
└──────────────────────────────────────┘
```

## 5.2 Backend Technology Stack

| Component | Technology |
|-----------|------------|
| Backend Framework | Spring Boot 3.2.2 |
| Language | Java 17 |
| Database | H2 in-memory database |
| ORM | Spring Data JPA / Hibernate |
| Security | Spring Security with HTTP Basic Auth |
| API Documentation | SpringDoc OpenAPI 3 / Swagger UI |
| Frontend | Pro-code frontend with static HTML, CSS and JavaScript |
| Build Tool | Maven |
| Runtime / Demo | Local machine or GitHub Codespaces |

## 5.3 Package Structure

```text
src/main/java/com/mobygo/carrental
├── MobyGoApplication.java
├── config
│   ├── CorsConfig.java
│   ├── DataInitializer.java
│   ├── OpenApiConfig.java
│   ├── PasswordConfig.java
│   └── SecurityConfig.java
├── controller
│   ├── CarController.java
│   ├── LocationController.java
│   ├── RentalController.java
│   ├── RentalRequest.java
│   ├── UserController.java
│   └── WelcomeController.java
├── exception
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── model
│   ├── Car.java
│   ├── CarCategory.java
│   ├── Location.java
│   ├── Rental.java
│   └── User.java
├── repository
│   ├── CarRepository.java
│   ├── LocationRepository.java
│   ├── RentalRepository.java
│   └── UserRepository.java
└── service
    ├── CarService.java
    ├── LocationService.java
    ├── RentalService.java
    └── UserService.java
```

Frontend files:

```text
src/main/resources/static
├── index.html
├── locations.html
├── rentals.html
├── admin.html
├── css/style.css
└── js/api.js
```

## 5.4 Design Patterns & Principles

| Principle / Pattern | Implementation |
|---------------------|----------------|
| **Layered Architecture** | Controllers, services, repositories, models, and database are separated. |
| **MVC / REST Controller Pattern** | REST controllers expose HTTP endpoints and delegate business logic to services. |
| **Repository Pattern** | Spring Data JPA repositories encapsulate persistence operations. |
| **Service Layer Pattern** | Rental business logic, validation, and price calculation are implemented in `RentalService`. |
| **DTO / Request Object** | `RentalRequest` separates the incoming booking request from the persisted `Rental` entity. |
| **DRY Principle** | Shared rate map and shared frontend functions avoid repeated logic. |
| **CRUD Paradigm** | Cars, locations, rentals, and users are exposed through list/create/update/delete-style endpoints. |
| **API Design Principles** | Resource-oriented URLs, HTTP verbs, JSON payloads, and Swagger documentation. |

---

# 6. Security

API-level security is implemented using **Spring Security** and **HTTP Basic Authentication**.

Public access is allowed for:

- Static frontend files
- `GET /api/`
- `GET /api/rates`
- `GET /api/cars/**`
- `GET /api/locations/**`
- `POST /api/rentals`
- Swagger UI and OpenAPI docs
- H2 console for development/demo purposes

All other endpoints require authentication.

The project uses BCrypt password encoding. Users are stored in the H2 database and loaded through `UserService`, which implements Spring Security's `UserDetailsService`.

---

# 7. Demonstrator — Installation & Running

## 7.1 Running Locally

### Prerequisites

- Java 17 or newer
- Maven 3.9 or newer

### Start the application

```bash
git clone https://github.com/KeremOezmenHUB/mobygo-car-rental.git
cd mobygo-car-rental
./mvnw spring-boot:run
```

Open the application:

| Resource | URL |
|----------|-----|
| Web App | `http://localhost:8080/` |
| Cars Page | `http://localhost:8080/` |
| Locations Page | `http://localhost:8080/locations.html` |
| Booking Page | `http://localhost:8080/rentals.html` |
| Admin Page | `http://localhost:8080/admin.html` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| H2 Console | `http://localhost:8080/h2-console` |

H2 Console settings:

| Field | Value |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:mobygo` |
| User Name | `sa` |
| Password | *(empty)* |

## 7.2 Running on GitHub Codespaces

1. Open the GitHub repository.
2. Click **Code → Codespaces → Create codespace on main**.
3. Wait until the workspace is ready.
4. Start the backend:

```bash
./mvnw spring-boot:run
```

5. Open the **Ports** tab in Codespaces.
6. Make port `8080` public if the frontend needs external access.
7. Open the forwarded URL.
8. Use the forwarded base URL for the web app or Swagger, for example:

```text
https://<your-codespace>-8080.app.github.dev/swagger-ui.html
```

## 7.3 Example API Request

Create a guest rental:

```bash
curl -X POST http://localhost:8080/api/rentals \
  -H "Content-Type: application/json" \
  -d '{
    "carId": 1,
    "pickupLocationId": 1,
    "dropoffLocationId": 2,
    "customerName": "Maria Müller",
    "customerEmail": "maria@example.com",
    "startDate": "2026-07-01",
    "endDate": "2026-07-05"
  }'
```

Expected result: The backend stores a rental and returns a JSON response that includes the calculated `totalPrice`.

---

# 8. Project Management

## 8.1 Milestones

| Milestone | Result |
|-----------|--------|
| **1. Analysis** | Scenario, actors, use cases, and user stories defined. |
| **2. Domain Design** | Domain model with cars, locations, rentals, users, and car categories designed. |
| **3. Frontend Implementation** | Responsive static frontend implemented with cars, locations, booking, and admin views. |
| **4. Business Logic & API Design** | Rental pricing, booking validation, REST endpoints, and OpenAPI structure defined. |
| **5. Data & API Implementation** | JPA entities, repositories, services, controllers, and seed data implemented. |
| **6. Security** | Basic Auth with seeded users and protected admin endpoints implemented. |
| **7. Demonstrator** | End-to-end app can be run locally or in Codespaces and demonstrated via browser and Swagger. |

## 8.2 Requirements Coverage

| Assessment Requirement | Fulfilment in MobyGo |
|------------------------|----------------------|
| At least three layers | Controller, service, repository, model/database layers. |
| At least two tiers | Frontend tier and backend/API/database tier. |
| At least four views | Cars, locations, booking, admin dashboard, Swagger, H2 console. |
| At least four entities | `Car`, `Location`, `Rental`, `User`. |
| Business logic | Price calculation, double-booking prevention, date validation, customer validation. |
| API-level security | Spring Security with HTTP Basic Auth. |
| OpenAPI documentation | Swagger UI available at `/swagger-ui.html`. |
| Version control | GitHub repository available at [mobygo-car-rental](https://github.com/KeremOezmenHUB/mobygo-car-rental). |
| Documentation/report | This README documents scenario, design, implementation, setup, and requirements coverage. |
| Presentation video | Video presentation available at [MobyGo video presentation](https://fhnw365-my.sharepoint.com/personal/ifin_anwar_students_fhnw_ch/_layouts/15/stream.aspx?id=%2Fpersonal%2Fifin%5Fanwar%5Fstudents%5Ffhnw%5Fch%2FDocuments%2FMobbyGo%2Emp4&referrer=StreamWebApp%2EWeb&referrerScenario=AddressBarCopied%2Eview%2E3b5a8d5a%2D1fbc%2D4eb2%2D9fe7%2D02412e530e66). |
| Frontend artefact | Pro-code frontend included in the repository under `src/main/resources/static/`. |

---

# 9. Default Credentials

The following users are inserted automatically at startup:

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | ADMIN |
| `john` | `user123` | USER |
| `anna` | `user123` | USER |

Use the admin account to access protected endpoints and the admin dashboard.

---

## Final Notes

MobyGo demonstrates a complete end-to-end web application with a clear business domain, responsive UI, REST API, business rules, persistence, authentication, and OpenAPI documentation. The project is prepared for demonstration through the included pro-code frontend, Swagger UI, and H2 database console.
