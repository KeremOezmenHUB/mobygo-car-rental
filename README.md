# [cite_start]FHNW Internet Technology Project: MobyGo Car Rental Web App [cite: 6]

[cite_start]**Institution:** FHNW University of Applied Sciences and Arts Northwestern Switzerland, School of Business[cite: 3, 4].
[cite_start]**Module:** Internet Technology[cite: 8].

**Group Members & Contributions:**
* **[Student 1 Name]** - [Detailed contribution, e.g., Frontend Implementation (Budibase), UX/UI Design]
* **[Student 2 Name]** - [Detailed contribution, e.g., Backend Architecture, Spring Boot, Business Logic]
* **[Student 3 Name]** - [Detailed contribution, e.g., Database Schema (H2), Basic Auth Security, OpenAPI Documentation]
[cite_start]*(Note: The names of the group work members and their contribution must be disclosed in the documentation[cite: 88].)*

**Project Deliverables & Links:**
* [cite_start]**Presentation Video (Max 10 min):** [Insert Link to MS Stream/SWITCHtube/YouTube here] [cite: 60, 73]
* [cite_start]**Deployed Web Application (GitHub Codespaces):** [Insert Link here] [cite: 72]
* [cite_start]**OpenAPI Documentation (Swagger):** [Insert Link here] [cite: 56]
* [cite_start]**Frontend Export Link:** [Insert Budibase export link here] [cite: 70]

---

## [cite_start]1. Analysis: Scenario and User Stories [cite: 77]
**Scenario Ideation & Use Case Analysis:** MobyGo is a comprehensive web application designed to manage short-term car rentals within a city network. Users can browse a catalog of available vehicles (Economy, SUV, Electric) located at various city stations and make reservations. Administrators have full control over the vehicle fleet, station management, and booking oversight.

**User Stories:**
[cite_start]The following user stories have been adapted from the minimal generic requirements to fit the MobyGo car rental use case[cite: 25]:
1.  [cite_start]**As an [admin]**, I want to have a Web app so that I can manage the fleet and stations on different mobile devices and on desktop computers[cite: 26].
2.  [cite_start]**As an [admin]**, I want to see a consistent visual appearance so that I can navigate easily between the fleet dashboard and bookings, and it looks consistent[cite: 27].
3.  [cite_start]**As an [admin]**, I want to use list views so that I can explore and read my business data (e.g., viewing all active reservations and available cars)[cite: 28].
4.  [cite_start]**As an [admin]**, I want to use edit and create views so that I can maintain my business data (e.g., adding a new car or updating a station's address)[cite: 29].
5.  [cite_start]**As an [admin]**, I want to log-in so that I can authenticate myself and securely access the management dashboard[cite: 30].
6.  [cite_start]**As a [user]**, I want to use list views so that I can access public pages to browse the catalog of available rental cars[cite: 31].
7.  [cite_start]**As a [user]**, I want to authenticate myself so that I can read my personal and confidential data, such as my upcoming and past bookings[cite: 32].

---

## [cite_start]2. Domain Design [cite: 78]

**Domain Model:**
[cite_start]To fulfill the requirement of at least four entities, our database schema consists of the following core components[cite: 39]:
* **Car:** Represents the vehicle available for rent (Attributes: ID, License Plate, Model, Category, Availability Status).
* **Station:** Represents the physical location where cars are stored and picked up (Attributes: ID, Name, City, Address). *Relationship: One station contains many cars.*
* **Booking:** Represents the reservation of a car by a user (Attributes: ID, Start Date, End Date, Total Price). *Relationship: One booking belongs to one car.*
* **User:** Represents the customer renting the vehicle or the admin managing the system (Attributes: ID, First Name, Last Name, Email, Role). *Relationship: One user can create multiple bookings.*

---

## [cite_start]3. Frontend Implementation [cite: 79]
**Design and Realization:**
[cite_start]The frontend is implemented using **Budibase**, a recommended low-code app design tool[cite: 46]. [cite_start]The design focuses on a consistent identity, adequate user experience, responsive layout, and well-chosen typography[cite: 43]. 

[cite_start]**Key Views (Meeting the minimum 4 views requirement [cite: 38]):**
1.  **Public Car Catalog (List View):** Displays available vehicles and their home stations for public users.
2.  **Admin Fleet Dashboard (List View):** Allows admins to monitor all cars and their current statuses.
3.  **Vehicle Management (Create/Edit View):** A form interface for admins to add new vehicles to the database or edit existing ones.
4.  **User Booking History (List View):** A personalized view for authenticated users to see their past and active reservations.

---

## [cite_start]4. Business Logic and API Design [cite: 80]
**Architecture & Design Principles:**
[cite_start]The application architecture reflects at least three different layers on at least two tiers (backend and frontend)[cite: 37]. [cite_start]The system strongly relies on OOP, design patterns, API design principles, routing functionalities, DRY, and CRUD paradigms[cite: 45].

**Business Rule Implementation:**
[cite_start]The service layer implements the following critical business rule corresponding to an operational constraint[cite: 40]: *A `Car` cannot be booked if it is already reserved during the selected time period, and a car must be returned to a `Station` before its status can be updated to "Available".*

---

## [cite_start]5. Data and API Implementation [cite: 81]
**Backend Technology:**
[cite_start]The backend is implemented using an enterprise-grade framework, specifically **Spring-Boot 3.0 and Java 17**[cite: 49]. 

**Database Technology:**
[cite_start]We are using **H2**, a lightweight in-memory relational database, which is recommended for demonstration purposes[cite: 51].

**Documentation & Version Control:**
[cite_start]All API endpoints in the web service are documented and made available using **OpenAPI 3.0 (Swagger)** documentation[cite: 56]. [cite_start]All source code and artifacts are maintained actively under version control using **GitHub**[cite: 53, 55].

---

## [cite_start]6. Security [cite: 83]
**API-Level Security:**
[cite_start]The application implements API-level security using **Basic Auth**[cite: 83]. 
* Public REST endpoints (e.g., retrieving the list of available cars) are open.
* Administrative endpoints (e.g., creating a new station or modifying a booking) require valid admin credentials to access.

---

## [cite_start]7. Demonstrator [cite: 84]
**Integration & Execution:**
[cite_start]The final demonstrator integrates the frontend and backend to realize an end-to-end application consuming REST APIs from the web service[cite: 84]. [cite_start]The published web application and web services are configured and running on **GitHub Codespaces**[cite: 72].
