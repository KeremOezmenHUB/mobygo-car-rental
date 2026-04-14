-- 1. Insert Stations
INSERT INTO stations (name, city, address) VALUES ('Zurich HB', 'Zurich', 'Bahnhofplatz 15');
INSERT INTO stations (name, city, address) VALUES ('Winterthur Station', 'Winterthur', 'Bahnhofplatz 1');
INSERT INTO stations (name, city, address) VALUES ('Basel SBB', 'Basel', 'Centralbahnstrasse 10');

-- 2. Insert Cars (station_id links to the stations above)
INSERT INTO cars (license_plate, model, category, status, station_id) VALUES ('ZH-12345', 'VW Golf', 'Economy', 'Available', 1);
INSERT INTO cars (license_plate, model, category, status, station_id) VALUES ('ZH-98765', 'Tesla Model 3', 'Electric', 'Available', 1);
INSERT INTO cars (license_plate, model, category, status, station_id) VALUES ('ZH-55555', 'Audi Q5', 'SUV', 'Available', 2);
INSERT INTO cars (license_plate, model, category, status, station_id) VALUES ('BS-11111', 'Toyota Yaris', 'Economy', 'Maintenance', 3);

-- 3. Insert Users
INSERT INTO app_users (first_name, last_name, email, role) VALUES ('Admin', 'User', 'admin@mobygo.ch', 'ADMIN');
INSERT INTO app_users (first_name, last_name, email, role) VALUES ('Alice', 'Smith', 'alice@mobygo.ch', 'USER');

-- 4. Insert Bookings (user_id and car_id link to the data above)
-- Format: YYYY-MM-DD
INSERT INTO bookings (start_date, end_date, total_price, user_id, car_id) VALUES ('2026-04-10', '2026-04-12', 150.00, 2, 3);