# MobyGo – Car Rental Web Application

> Internet Technology Group Project — FHNW School of Business

| | |
|---|---|
| **Team** | *[Add team member names here]* |
| **Video** | *[Add link to presentation video]* |
| **Live App** | *[Add Budibase link]* |
| **API Docs** | `http://localhost:8080/swagger-ui.html` (or Codespace URL) |

---

## 1. Analysis

### Scenario

MobyGo is a car rental platform that allows customers to browse available vehicles, check pricing, and book cars across multiple locations in Switzerland. Admins manage the fleet, locations, and bookings through a secure backend.

### User Stories

| # | Actor | Story |
|---|---|---|
| 1 | Admin | I want to use the web app on mobile and desktop so it works everywhere |
| 2 | Admin | I want a consistent visual design so I can navigate easily |
| 3 | Admin | I want list views for cars, locations, rentals and users so I can manage the fleet |
| 4 | Admin | I want to create and edit cars and locations so I can maintain the inventory |
| 5 | Admin | I want to log in so only I can access admin functions |
| 6 | User | I want to browse available cars and locations without logging in |
| 7 | User | I want to create a rental booking so I can reserve a car |

---

## 2. Domain Design

### Domain Model

```
┌─────────────┐       ┌──────────────┐
│    User     │       │   Location   │
│─────────────│       │──────────────│
│ id          │       │ id           │
│ username    │       │ name         │
│ password    │       │ city         │
│ role        │       │ address      │
└──────┬──────┘       └──────┬───────┘
       │                     │ 1
       │ 1                   │
       │                  *  │
       │              ┌──────┴──────┐
       │              │     Car     │
       │              │─────────────│
       │              │ id          │
       │              │ brand       │
       │              │ model       │
       │              │ licensePlate│
       │              │ category    │◄── CarCategory (enum)
       └──────────────┤─────────────│    ECONOMY / COMPACT
            *         └─────────────┘    SUV / LUXURY / VAN
       ┌──────────────────────────────┐
       │           Rental             │
       │──────────────────────────────│
       │ id                           │
       │ user (FK)                    │
       │ car (FK)                     │
       │ pickupLocation (FK)          │
       │ dropoffLocation (FK)         │
       │ startDate                    │
       │ endDate                      │
       │ totalPrice                   │
       └──────────────────────────────┘
```

**Entities:** `Car`, `Location`, `Rental`, `User`

---

## 3. Frontend Implementation

The frontend is built with **Budibase** and connects to the REST API.

### Views (4 required)

| View | Type | Description |
|---|---|---|
| **Cars** | List + Detail | Browse all cars, filter by location/category |
| **Locations** | List + Detail | View all rental locations with available cars |
| **Rentals** | List + Create Form | Book a car, view existing bookings |
| **Admin Dashboard** | Edit/Create | Manage cars, locations, users (admin only) |

**Budibase Setup:**
1. Create new app at [budibase.app](https://budibase.com)
2. Add REST API data source pointing to `https://<codespace>-8080.app.github.dev/api`
3. Add Basic Auth header: `Authorization: Basic <base64(admin:admin123)>`
4. Create the four views above using the auto-generated screens

---

## 4. Business Logic and API Design

### Business Rules

**Rule 1 — Price Calculation:**
The rental price is automatically calculated based on the car category and number of days:

| Category | Daily Rate (CHF) |
|---|---|
| ECONOMY | 50 |
| COMPACT | 80 |
| SUV | 120 |
| LUXURY | 200 |
| VAN | 100 |

`totalPrice = dailyRate × (endDate - startDate)`

**Rule 2 — No Double Booking:**
A car cannot be rented if it overlaps with an existing rental. The API returns HTTP 409 Conflict with a descriptive message.

### API Endpoints

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/api/` | ❌ | Welcome message |
| GET | `/api/rates` | ❌ | Daily rates per category |
| GET | `/api/cars` | ❌ | List all cars |
| GET | `/api/cars?locationId=1` | ❌ | Filter by location |
| GET | `/api/cars?category=SUV` | ❌ | Filter by category |
| GET | `/api/cars/{id}` | ❌ | Get single car |
| POST | `/api/cars` | ✅ | Create car |
| PUT | `/api/cars/{id}` | ✅ | Update car |
| DELETE | `/api/cars/{id}` | ✅ | Delete car |
| GET | `/api/locations` | ❌ | List all locations |
| GET | `/api/locations/{id}` | ❌ | Get single location |
| POST | `/api/locations` | ✅ | Create location |
| PUT | `/api/locations/{id}` | ✅ | Update location |
| DELETE | `/api/locations/{id}` | ✅ | Delete location |
| GET | `/api/rentals` | ✅ | List all rentals |
| GET | `/api/rentals/user/{username}` | ✅ | Rentals for a user |
| POST | `/api/rentals` | ✅ | Create rental |
| GET | `/api/users` | ✅ | List all users |
| POST | `/api/users` | ✅ | Create user |
| DELETE | `/api/users/{id}` | ✅ | Delete user |

Full interactive docs: `/swagger-ui.html`

---

## 5. Data and API Implementation

### Architecture

```
┌─────────────────────────────────────┐
│           Frontend (Budibase)        │  Tier 1
└───────────────┬─────────────────────┘
                │ REST / HTTP
┌───────────────▼─────────────────────┐
│         Controller Layer             │  Tier 2 / Layer 1
│  CarController, LocationController   │
│  RentalController, UserController    │
├─────────────────────────────────────┤
│          Service Layer               │  Layer 2
│  Business logic, price calculation   │
│  double-booking validation           │
├─────────────────────────────────────┤
│         Repository Layer             │  Layer 3
│  Spring Data JPA repositories        │
├─────────────────────────────────────┤
│          H2 Database                 │
└─────────────────────────────────────┘
```

### Technology Stack

| Component | Technology |
|---|---|
| Backend | Spring Boot 3.2.2 |
| Language | Java 17 |
| Database | H2 (in-memory) |
| ORM | Spring Data JPA / Hibernate |
| Security | Spring Security (Basic Auth) |
| API Docs | SpringDoc OpenAPI 3.0 (Swagger UI) |
| Frontend | Budibase |
| CI/CD | GitHub Codespaces |

---

## 6. Security

API-level security is implemented using **HTTP Basic Authentication** via Spring Security.

- `ROLE_ADMIN` — full access to all endpoints
- `ROLE_USER` — access to booking endpoints
- Public — read-only access to cars, locations, and rates

Default credentials (seeded at startup):

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | ADMIN |
| `john` | `user123` | USER |
| `anna` | `user123` | USER |

---

## 7. Demonstrator — Running the Application

### Option A: GitHub Codespaces (recommended)

1. Open the repository on GitHub
2. Click **Code → Codespaces → Create codespace on main**
3. Wait for the container to start
4. In the terminal: `mvn spring-boot:run`
5. Go to **Ports** tab → make port `8080` **Public**
6. Copy the forwarded URL → use in Budibase as the API base URL

### Option B: Local Setup

```bash
# Prerequisites: Java 17+, Maven 3.9+
git clone https://github.com/<your-org>/mobygo.git
cd mobygo
mvn spring-boot:run
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:mobygo`)

### Example: Create a Rental

```bash
curl -X POST http://localhost:8080/api/rentals \
  -u john:user123 \
  -H "Content-Type: application/json" \
  -d '{
    "carId": 1,
    "pickupLocationId": 1,
    "dropoffLocationId": 2,
    "userId": 2,
    "startDate": "2026-07-01",
    "endDate": "2026-07-05"
  }'
```

Response includes `totalPrice: 200.0` (ECONOMY × 4 days × 50 CHF).

---

## Group Composition

| Name | Contribution |
|---|---|
| *[Member 1]* | Backend, API design, Spring Boot |
| *[Member 2]* | Frontend, Budibase, UI/UX |
| *[Member 3]* | Documentation, README, video |
| *[Member 4]* | Testing, deployment, Codespaces |

