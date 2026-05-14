# MobyGo — Car Rental Web Application

**Institution:** FHNW University of Applied Sciences and Arts Northwestern Switzerland, School of Business  
**Module:** Internet Technology

**Group Members:**
| Name | Contribution |
|------|-------------|
| Ifin Anwar | Frontend Implementation, UX/UI Design |
| Bajram Kurto Elezi | Backend Architecture, Spring Boot, Business Logic |
| Kerem Özmen | Database Schema (H2), Basic Auth Security, OpenAPI Documentation |

**Project Deliverables:**
| Artifact | Link |
|----------|------|
| Presentation Video (max 10 min) | _[to be added]_ |
| Deployed Web App (GitHub Codespaces) | _[to be added after launch]_ |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| GitHub Repository | https://github.com/KeremOezmenHUB/mobygo-car-rental |

---

## Quick Start

### Option A — GitHub Codespaces (recommended)

1. Click **Code → Codespaces → Create codespace on main** in the GitHub repository.
2. The devcontainer starts the backend and the frontend automatically.
3. VS Code will pop a notification: **Open in Browser** for port 5500 (frontend).
4. The backend API is available on the forwarded port 8080.

### Option B — Local Setup

**Prerequisites:** Java 17+, Maven 3.9+, any HTTP server (Python 3 works)

```bash
# 1. Clone
git clone https://github.com/KeremOezmenHUB/mobygo-car-rental.git
cd mobygo-car-rental

# 2. Start backend
cd backend
mvn spring-boot:run

# 3. In a new terminal — start frontend
cd ../frontend
python3 -m http.server 5500
# open http://localhost:5500
```

**Admin credentials:** `admin` / `admin123`  
**H2 Console:** http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:mobygo`)

---

## Repository Structure

```
mobygo-car-rental/
├── .devcontainer/
│   ├── devcontainer.json      # Codespaces / VS Code dev container config
│   └── start.sh               # Starts backend + frontend automatically
├── backend/                   # Spring Boot application (tier 2)
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/mobygo/car_rental/
│       │   ├── config/        # SecurityConfig, CorsConfig
│       │   ├── controller/    # REST controllers (API layer)
│       │   ├── exception/     # GlobalExceptionHandler
│       │   ├── model/         # JPA entities (domain layer)
│       │   ├── repository/    # Spring Data JPA repositories (persistence layer)
│       │   └── service/       # Business logic (service layer)
│       └── resources/
│           ├── application.properties
│           └── data.sql       # Seed data (4 stations, 4 users, 14 cars, 4 bookings)
└── frontend/                  # Vanilla HTML/CSS/JS (tier 1)
    ├── index.html             # Car catalog + booking modal
    ├── stations.html          # Station list with per-station stats
    ├── user-bookings.html     # User reservation history
    ├── admin-dashboard.html   # Admin fleet management
    ├── car-form.html          # Add new vehicle form
    ├── style.css              # Design system (CSS variables, responsive)
    └── app.js                 # All frontend logic, API calls, routing
```

---

## 1. Analysis — Scenario and User Stories

**Scenario:** MobyGo is a Swiss urban car-sharing service. Customers can browse a catalogue of electric, hybrid and city vehicles at stations across Switzerland and make short-term reservations online. Administrators manage the fleet, stations and bookings through a protected dashboard.

**User Stories:**

| # | Role | Story |
|---|------|-------|
| 1 | Admin | I want a web app so I can manage the fleet and stations on any device. |
| 2 | Admin | I want a consistent visual appearance so I can navigate the dashboard intuitively. |
| 3 | Admin | I want list views so I can browse all vehicles, their statuses, and reservations. |
| 4 | Admin | I want create and edit views so I can add new cars and update existing records. |
| 5 | Admin | I want to log in with Basic Auth so I can access protected management endpoints. |
| 6 | User | I want to browse the public car catalogue without logging in. |
| 7 | User | I want to view my personal booking history after authenticating. |

---

## 2. Domain Design

### Entity Relationship

```
Station  1 ──< Car  1 ──< Booking >── 1  User
```

### Entities (≥ 4 required)

| Entity | Key Attributes | Relationships |
|--------|---------------|---------------|
| **Car** | id, licensePlate, model, category (`Electric`/`Hybrid`/`City`), status (`Available`/`Rented`/`Maintenance`) | Many-to-one → Station |
| **Station** | id, name, city, address | One-to-many → Car |
| **Booking** | id, startDate, endDate, totalPrice | Many-to-one → Car; Many-to-one → User |
| **User** | id, firstName, lastName, email, role (`USER`/`ADMIN`) | One-to-many → Booking |

### Domain Model (UML)

```
┌────────────┐        ┌──────────────┐
│  Station   │ 1    * │     Car      │
│────────────│────────│──────────────│
│ id         │        │ id           │
│ name       │        │ licensePlate │
│ city       │        │ model        │
│ address    │        │ category     │
└────────────┘        │ status       │
                      │ station_id   │
                      └──────┬───────┘
                             │ 1
                             │
                             │ *
                      ┌──────┴───────┐        ┌────────────┐
                      │   Booking    │  *   1  │    User    │
                      │──────────────│─────────│────────────│
                      │ id           │         │ id         │
                      │ startDate    │         │ firstName  │
                      │ endDate      │         │ lastName   │
                      │ totalPrice   │         │ email      │
                      │ car_id       │         │ role       │
                      │ user_id      │         └────────────┘
                      └──────────────┘
```

---

## 3. Frontend Implementation

**Approach:** Pro-code — pure HTML5, CSS3, and vanilla JavaScript (confirmed with lecturer in coaching session 1). No frontend framework or build tool is required.

### Views (≥ 4 required — 5 delivered)

| File | View | Access | Description |
|------|------|--------|-------------|
| `index.html` | Car Catalogue | Public | Lists all cars with filter bar (All / Electric / Hybrid / City); opens booking modal |
| `stations.html` | Stations Map | Public | All MobyGo stations with total / available / unavailable car counts |
| `user-bookings.html` | My Bookings | Authenticated user | Personal reservation history |
| `admin-dashboard.html` | Fleet Dashboard | Admin | Stats grid, full vehicle table, inline edit modal, delete action |
| `car-form.html` | Add Vehicle | Admin | Form to register a new car to a station |

### Design System (`style.css`)

- **Font:** Inter (Google Fonts)
- **CSS Variables:** `--accent` (green), `--navy` (dark), `--surface`, `--border`, `--radius-*`
- **Components:** Nav, hero section, filter bar, car card with category stripe, booking modal, edit modal, toast notifications, skeleton loaders, station cards, responsive table
- **Responsive:** Mobile-first; single-column layout at ≤640 px

### Frontend Architecture (`app.js`)

```
app.js
 ├── resolveApiBase()       — auto-detects Codespaces vs local hostname
 ├── apiRequest()           — fetch wrapper with auto Basic-Auth for mutations
 ├── toast()                — temporary notification banners
 ├── renderSkeletons()      — shimmer placeholders during loading
 │
 ├── initCatalogPage()      — index.html: fetch cars, render cards, filters, booking modal
 ├── initStationsPage()     — stations.html: parallel fetch stations+cars, render stats
 ├── initBookingsPage()     — user-bookings.html: fetch and render booking history
 ├── initAdminPage()        — admin-dashboard.html: stats, table, edit/delete
 └── initCarForm()          — car-form.html: POST new car, redirect on success
```

**Codespaces URL detection:**
```js
function resolveApiBase() {
    const { hostname, protocol } = window.location;
    if (hostname.includes('.app.github.dev')) {
        return `${protocol}//${hostname.replace(/-\d+\.app\.github\.dev/, '-8080.app.github.dev')}/api`;
    }
    return 'http://localhost:8080/api';
}
```

---

## 4. Business Logic and API Design

### Three-Layer Architecture on Two Tiers

```
Tier 1 — Frontend (Browser)       Tier 2 — Backend (JVM / Spring Boot)
────────────────────────          ─────────────────────────────────────
  Presentation Layer               Controller Layer   (REST API)
  (HTML / CSS / JS)    ←──HTTP──▶  Service Layer      (Business Logic)
                                   Persistence Layer  (Spring Data JPA)
                                        │
                                      H2 DB (in-memory)
```

### Business Rules

1. **Booking conflict check** — a car cannot be booked if an existing booking overlaps the requested date range. The service layer rejects overlapping reservations with HTTP 409 Conflict.
2. **Status guard** — a car's status can only be set to `Available` if it has no active bookings in the current date range. Setting a booked car to `Available` is rejected with HTTP 400.
3. **Price calculation** — `totalPrice = days × CHF 45` (Electric), `days × CHF 35` (Hybrid), `days × CHF 25` (City). Calculated server-side in `BookingService`.

### REST API Endpoints

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/api/cars` | Public | List all cars |
| GET | `/api/cars/{id}` | Public | Get single car |
| POST | `/api/cars` | Admin | Add new car |
| PUT | `/api/cars/{id}` | Admin | Update car |
| DELETE | `/api/cars/{id}` | Admin | Delete car |
| GET | `/api/stations` | Public | List all stations |
| GET | `/api/stations/{id}` | Public | Get single station |
| POST | `/api/stations` | Admin | Create station |
| GET | `/api/bookings` | Admin | List all bookings |
| GET | `/api/bookings/user/{userId}` | User | Get bookings for user |
| POST | `/api/bookings` | User | Create booking |
| DELETE | `/api/bookings/{id}` | Admin | Cancel booking |
| GET | `/api/users` | Admin | List all users |

Full interactive documentation: **http://localhost:8080/swagger-ui.html**

---

## 5. Data and API Implementation

### Backend Stack

| Component | Technology |
|-----------|-----------|
| Framework | Spring Boot 4.0.5 |
| Language | Java 17 |
| Database | H2 (in-memory, `jdbc:h2:mem:mobygo`) |
| ORM | Spring Data JPA / Hibernate |
| API Docs | SpringDoc OpenAPI 2.4.0 (Swagger UI) |
| Security | Spring Security (Basic Auth) |
| Build | Maven 3.9 |

### Seed Data (`data.sql`)

- **4 Stations:** Zürich HB, Basel SBB, Bern Bahnhof, Geneva Aéroport
- **4 Users:** 3 regular users + 1 admin (`admin@mobygo.ch` / `admin123`)
- **14 Cars:** Electric (5), Hybrid (5), City (4) across all stations
- **4 Bookings:** Past and upcoming sample reservations

### Exception Handling

`GlobalExceptionHandler` (`@RestControllerAdvice`) maps Java exceptions to HTTP status codes with JSON error bodies:

```json
{ "timestamp": "2025-06-01T10:00:00", "error": "Car is already booked for this period" }
```

| Exception | HTTP Status |
|-----------|-------------|
| `IllegalStateException` | 409 Conflict |
| `IllegalArgumentException` | 400 Bad Request |
| `EntityNotFoundException` | 404 Not Found |
| `Exception` | 500 Internal Server Error |

---

## 6. Security

**Mechanism:** HTTP Basic Authentication via Spring Security

### Access Matrix

| Endpoint Pattern | GET | POST / PUT / DELETE |
|-----------------|-----|---------------------|
| `/api/cars/**` | Public | Admin only |
| `/api/stations/**` | Public | Admin only |
| `/api/bookings/**` | Authenticated | Authenticated |
| `/api/users/**` | Admin only | Admin only |
| `/swagger-ui/**`, `/v3/api-docs/**` | Public | — |
| `/h2-console/**` | Admin only | — |

**CORS:** Configured to allow all origins (`allowedOriginPatterns("*")`) with credentials, enabling both local development and GitHub Codespaces (`*.app.github.dev`) without manual reconfiguration.

**Default credentials (demo only):**
- Admin: `admin` / `admin123`
- User: `user1` / `password`

---

## 7. Demonstrator

### GitHub Codespaces

The project ships with a fully configured dev container (`.devcontainer/`):

- **Base image:** `mcr.microsoft.com/devcontainers/java:21-jdk-bookworm`
- **Port 8080** → Backend REST API + Swagger UI
- **Port 5500** → Frontend (Python HTTP server)
- **`postStartCommand`** runs `.devcontainer/start.sh` which boots both services automatically
- VS Code extensions pre-installed: Java Pack, Spring Boot Dashboard, Live Server, REST Client

### End-to-End Flow

```
User opens frontend (port 5500)
  └─▶ app.js resolveApiBase() detects Codespaces hostname
  └─▶ All API calls routed to port 8080 automatically
  └─▶ Spring Security validates credentials per endpoint
  └─▶ Spring Boot queries H2, returns JSON
  └─▶ Frontend renders cards / modals / toasts
```

### Demo Walkthrough

1. **Browse catalogue** — `index.html` shows all cars; filter by category (Electric / Hybrid / City)
2. **Make a booking** — click "Book Now" on any available car; pick dates; confirm
3. **View my bookings** — `user-bookings.html` lists all reservations for the logged-in user
4. **Stations overview** — `stations.html` shows all locations with live availability stats
5. **Admin dashboard** — `admin-dashboard.html` shows fleet stats; edit or delete any vehicle
6. **Add a car** — `car-form.html` registers a new vehicle to a station
7. **Swagger UI** — http://localhost:8080/swagger-ui.html — test all endpoints interactively

---

## Development Notes

- The H2 database is **in-memory**: data resets on every backend restart. This is intentional for demo purposes.
- The frontend uses **no build tool** — open `frontend/index.html` directly or serve with any static file server.
- To run tests: `cd backend && mvn test`
- To build a fat JAR: `cd backend && mvn package` → `target/car-rental-0.0.1-SNAPSHOT.jar`
