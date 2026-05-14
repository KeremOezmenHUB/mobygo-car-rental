-- MobyGo Car Rental — Seed Data
-- Categories align with frontend filter buttons: Electric | Hybrid | City

-- ── Stations ──────────────────────────────────────────────────────────────
INSERT INTO stations (name, city, address) VALUES
('MobyGo Zürich-City',    'Zürich', 'Bahnhofstrasse 50, 8001 Zürich'),
('MobyGo Zürich-Airport', 'Zürich', 'Flughafenstrasse 110, 8058 Zürich'),
('MobyGo Bern',           'Bern',   'Marktgasse 25, 3011 Bern'),
('MobyGo Genf',           'Genf',   'Rue du Mont-Blanc 100, 1201 Genf');

-- ── Users ─────────────────────────────────────────────────────────────────
INSERT INTO app_users (first_name, last_name, email, role) VALUES
('Max',    'Müller',  'max.mueller@mobygo.ch',  'USER'),
('Anna',   'Schmidt', 'anna.schmidt@mobygo.ch', 'USER'),
('Peter',  'Keller',  'peter.keller@mobygo.ch', 'ADMIN'),
('Sandra', 'Weber',   'sandra.weber@mobygo.ch', 'USER');

-- ── Cars — Zürich-City (Station 1) ────────────────────────────────────────
INSERT INTO cars (license_plate, model, category, status, station_id) VALUES
('ZH-100001', 'Tesla Model 3',      'Electric', 'Available',   1),
('ZH-100002', 'BMW iX',             'Electric', 'Available',   1),
('ZH-100003', 'Toyota RAV4 Hybrid', 'Hybrid',   'Rented',      1),
('ZH-100004', 'Fiat 500e',          'City',     'Available',   1),
('ZH-100005', 'VW Golf eHybrid',    'Hybrid',   'Maintenance', 1);

-- ── Cars — Zürich-Airport (Station 2) ────────────────────────────────────
INSERT INTO cars (license_plate, model, category, status, station_id) VALUES
('ZH-200001', 'Peugeot e-208',   'Electric', 'Available', 2),
('ZH-200002', 'Renault Zoe',     'Electric', 'Rented',    2),
('ZH-200003', 'Smart #1',        'City',     'Available', 2),
('ZH-200004', 'Hyundai Ioniq 5', 'Electric', 'Available', 2);

-- ── Cars — Bern (Station 3) ──────────────────────────────────────────────
INSERT INTO cars (license_plate, model, category, status, station_id) VALUES
('BE-300001', 'Polestar 2',         'Electric', 'Available', 3),
('BE-300002', 'Toyota Yaris Cross', 'Hybrid',   'Available', 3),
('BE-300003', 'MINI Electric',      'City',     'Available', 3);

-- ── Cars — Genf (Station 4) ──────────────────────────────────────────────
INSERT INTO cars (license_plate, model, category, status, station_id) VALUES
('GE-400001', 'Skoda Enyaq',     'Electric', 'Available', 4),
('GE-400002', 'Honda Jazz Hybrid','Hybrid',  'Available', 4);

-- ── Sample Bookings ───────────────────────────────────────────────────────
-- Car 3 (RAV4 Hybrid, Rented) booked by Max
INSERT INTO bookings (start_date, end_date, total_price, user_id, car_id) VALUES
('2025-05-10', '2025-05-15', 250.00, 1, 3);

-- Car 7 (Renault Zoe, Rented) booked by Anna
INSERT INTO bookings (start_date, end_date, total_price, user_id, car_id) VALUES
('2025-05-12', '2025-05-14', 100.00, 2, 7);

-- Completed bookings for history view (user 2 = Anna)
INSERT INTO bookings (start_date, end_date, total_price, user_id, car_id) VALUES
('2025-04-01', '2025-04-05', 200.00, 2, 1),
('2025-04-20', '2025-04-22', 100.00, 2, 6);
