-- Test-Daten für MobyGo Car Rental
-- Speichere diese Datei unter: src/main/resources/data.sql

-- Stationen erstellen
INSERT INTO stations (name, city, address) VALUES
('MobyGo Zürich-City', 'Zürich', 'Bahnhofstrasse 50, 8001 Zürich'),
('MobyGo Zürich-Airport', 'Zürich', 'Flughafenstrasse 110, 8058 Zürich'),
('MobyGo Bern', 'Bern', 'Marktgasse 25, 3011 Bern'),
('MobyGo Genf', 'Genf', 'Rue du Mont-Blanc 100, 1201 Genf');

-- Benutzer erstellen
INSERT INTO app_users (first_name, last_name, email, role) VALUES
('Max', 'Müller', 'max.mueller@example.com', 'USER'),
('Anna', 'Schmidt', 'anna.schmidt@example.com', 'USER'),
('Peter', 'Keller', 'peter.keller@example.com', 'ADMIN'),
('Sandra', 'Weber', 'sandra.weber@example.com', 'USER');

-- Autos erstellen (verschiedene Statuse zum Testen)
-- Autos in Zürich-City (Station ID 1)
INSERT INTO cars (license_plate, model, category, status, station_id) VALUES
('ZH-123456', 'Toyota Corolla', 'Economy', 'Available', 1),
('ZH-123457', 'Toyota Corolla', 'Economy', 'Available', 1),
('ZH-234567', 'Mercedes C-Klasse', 'Sedan', 'Rented', 1),
('ZH-345678', 'Tesla Model 3', 'Electric', 'Available', 1),
('ZH-456789', 'BMW X5', 'SUV', 'Maintenance', 1);

-- Autos in Zürich-Airport (Station ID 2)
INSERT INTO cars (license_plate, model, category, status, station_id) VALUES
('ZH-567890', 'VW Golf', 'Economy', 'Available', 2),
('ZH-678901', 'Audi A4', 'Sedan', 'Available', 2),
('ZH-789012', 'Range Rover', 'SUV', 'Rented', 2);

-- Autos in Bern (Station ID 3)
INSERT INTO cars (license_plate, model, category, status, station_id) VALUES
('BE-123456', 'Toyota Yaris', 'Economy', 'Available', 3),
('BE-234567', 'Fiat 500', 'Economy', 'Available', 3),
('BE-345678', 'Porsche 911', 'Sports', 'Available', 3);

-- Beispiel-Buchungen (für Überschneidungs-Tests)
-- Buchung für Auto ID 3 (Mercedes, Rented)
INSERT INTO bookings (start_date, end_date, total_price, user_id, car_id) VALUES
('2024-12-20', '2024-12-25', 300.0, 1, 3);

-- Buchung für Auto ID 7 (Range Rover, Rented)
INSERT INTO bookings (start_date, end_date, total_price, user_id, car_id) VALUES
('2024-12-18', '2024-12-22', 200.0, 2, 7);

-- Weitere Buchungen für Test-Szenarien
INSERT INTO bookings (start_date, end_date, total_price, user_id, car_id) VALUES
('2024-11-10', '2024-11-15', 250.0, 3, 1),
('2024-11-20', '2024-11-25', 280.0, 2, 4);

-- ========================================
-- TEST-SZENARIOS
-- ========================================

-- SZENARIO 1: Erfolgreiche Buchung
-- Auto ID 1 (Available) buchen für 2024-12-10 bis 2024-12-15
-- POST /api/bookings
-- {
--   "user": { "id": 1 },
--   "car": { "id": 1 },
--   "startDate": "2024-12-10",
--   "endDate": "2024-12-15"
-- }
-- ERGEBNIS: 201 CREATED ✓

-- SZENARIO 2: Auto nicht verfügbar
-- Auto ID 3 (Rented) buchen für 2024-12-20 bis 2024-12-25
-- POST /api/bookings
-- {
--   "user": { "id": 2 },
--   "car": { "id": 3 },
--   "startDate": "2024-12-20",
--   "endDate": "2024-12-25"
-- }
-- ERGEBNIS: 409 CONFLICT - "Current status is 'Rented'" ✗

-- SZENARIO 3: Termine überschneiden sich
-- Auto ID 1 buchen für 2024-11-12 bis 2024-11-18
-- (überschneidet sich mit bestehender Buchung 2024-11-10 bis 2024-11-15)
-- POST /api/bookings
-- {
--   "user": { "id": 4 },
--   "car": { "id": 1 },
--   "startDate": "2024-11-12",
--   "endDate": "2024-11-18"
-- }
-- ERGEBNIS: 409 CONFLICT - "overlapping bookings" ✗

-- SZENARIO 4: Verfügbarkeitsprüfung
-- GET /api/bookings/availability/1?startDate=2024-12-10&endDate=2024-12-15
-- ERGEBNIS: { "carId": 1, "available": true } ✓

-- SZENARIO 5: Alle Autos einer Station abrufen
-- GET /api/cars/station/1
-- ERGEBNIS: 5 Autos aus Zürich-City

-- SZENARIO 6: Verfügbare Autos abrufen
-- GET /api/cars/status/Available
-- ERGEBNIS: Liste aller verfügbaren Autos

-- SZENARIO 7: Buchungshistorie eines Users
-- GET /api/bookings/user/1
-- ERGEBNIS: Alle Buchungen von Max Müller
