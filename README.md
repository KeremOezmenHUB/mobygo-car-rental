# FHNW Internet Technology Project: MobyGo Car Rental Web App

**Institution:** FHNW University of Applied Sciences and Arts Northwestern Switzerland, School of Business.
**Module:** Internet Technology.

**Group Members & Contributions:**
* **[Student 1 Name]** - [Detailed contribution, e.g., Frontend Implementation (Budibase), UX/UI Design]
* **[Student 2 Name]** - [Detailed contribution, e.g., Backend Architecture, Spring Boot, Business Logic]
* **[Student 3 Name]** - [Detailed contribution, e.g., Database Schema (H2), Basic Auth Security, OpenAPI Documentation]
*(Note: The names of the group work members and their contribution must be disclosed in the documentation.)*

**Project Deliverables & Links:**
* **Presentation Video (Max 10 min):** [Insert Link to MS Stream/SWITCHtube/YouTube here]
* **Deployed Web Application (GitHub Codespaces):** [Insert Link here]
* **OpenAPI Documentation (Swagger):** [Insert Link here]
* **Frontend Export Link:** [Insert Budibase export link here]

---

## 1. Analysis: Scenario and User Stories
**Scenario Ideation & Use Case Analysis:** MobyGo is a comprehensive web application designed to manage short-term car rentals within a city network. Users can browse a catalog of available vehicles (Economy, SUV, Electric) located at various city stations and make reservations. Administrators have full control over the vehicle fleet, station management, and booking oversight.

**User Stories:**
The following user stories have been adapted from the minimal generic requirements to fit the MobyGo car rental use case:
1.  **As an [admin]**, I want to have a Web app so that I can manage the fleet and stations on different mobile devices and on desktop computers.
2.  **As an [admin]**, I want to see a consistent visual appearance so that I can navigate easily between the fleet dashboard and bookings, and it looks consistent.
3.  **As an [admin]**, I want to use list views so that I can explore and read my business data (e.g., viewing all active reservations and available cars).
4.  **As an [admin]**, I want to use edit and create views so that I can maintain my business data (e.g., adding a new car or updating a station's address).
5.  **As an [admin]**, I want to log-in so that I can authenticate myself and securely access the management dashboard.
6.  **As a [user]**, I want to use list views so that I can access public pages to browse the catalog of available rental cars.
7.  **As a [user]**, I want to authenticate myself so that I can read my personal and confidential data, such as my upcoming and past bookings.

---

## 2. Domain Design


**Domain Model:**
To fulfill the requirement of at least four entities, our database schema consists of the following core components:
* **Car:** Represents the vehicle available for rent (Attributes: ID, License Plate, Model, Category, Availability Status).
* **Station:** Represents the physical location where cars are stored and picked up (Attributes: ID, Name, City, Address). *Relationship: One station contains many cars.*
* **Booking:** Represents the reservation of a car by a user (Attributes: ID, Start Date, End Date, Total Price). *Relationship: One booking belongs to one car.*
* **User:** Represents the customer renting the vehicle or the admin managing the system (Attributes: ID, First Name, Last Name, Email, Role). *Relationship: One user can create multiple bookings.*

---

## 3. Frontend Implementation
**Design and Realization:**
The frontend is implemented using **Budibase**, a recommended low-code app design tool. The design focuses on a consistent identity, adequate user experience, responsive layout, and well-chosen typography. 

**Key Views (Meeting the minimum 4 views requirement):**
1.  **Public Car Catalog (List View):** Displays available vehicles and their home stations for public users.
2.  **Admin Fleet Dashboard (List View):** Allows admins to monitor all cars and their current statuses.
3.  **Vehicle Management (Create/Edit View):** A form interface for admins to add new vehicles to the database or edit existing ones.
4.  **User Booking History (List View):** A personalized view for authenticated users to see their past and active reservations.

---

## 4. Business Logic and API Design
**Architecture & Design Principles:**
The application architecture reflects at least three different layers on at least two tiers (backend and frontend). The system strongly relies on OOP, design patterns, API design principles, routing functionalities, DRY, and CRUD paradigms.

**Business Rule Implementation:**
The service layer implements the following critical business rule corresponding to an operational constraint: *A `Car` cannot be booked if it is already reserved during the selected time period, and a car must be returned to a `Station` before its status can be updated to "Available".*

---

## 5. Data and API Implementation
**Backend Technology:**
The backend is implemented using an enterprise-grade framework, specifically **Spring-Boot 3.0 and Java 17**. 

**Database Technology:**
We are using **H2**, a lightweight in-memory relational database, which is recommended for demonstration purposes.

**Documentation & Version Control:**
All API endpoints in the web service are documented and made available using **OpenAPI 3.0 (Swagger)** documentation. All source code and artifacts are maintained actively under version control using **GitHub**.

---

## 6. Security
**API-Level Security:**
The application implements API-level security using **Basic Auth**. 
* Public REST endpoints (e.g., retrieving the list of available cars) are open.
* Administrative endpoints (e.g., creating a new station or modifying a booking) require valid admin credentials to access.

---

## 7. Demonstrator
**Integration & Execution:**
The final demonstrator integrates the frontend and backend to realize an end-to-end application consuming REST APIs from the web service. The published web application and web services are configured and running on **GitHub Codespaces**.
