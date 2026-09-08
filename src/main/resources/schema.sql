CREATE DATABASE IF NOT EXISTS hotel_db;

USE hotel_db;


-- ==========================================
-- CUSTOMER TABLE
-- ==========================================

CREATE TABLE IF NOT EXISTS customers (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    phone VARCHAR(20) NOT NULL,

    email VARCHAR(150) NOT NULL UNIQUE

);


-- ==========================================
-- ROOM TABLE
-- ==========================================

CREATE TABLE IF NOT EXISTS rooms (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    room_number VARCHAR(20) NOT NULL UNIQUE,

    room_type VARCHAR(20) NOT NULL,

    price_per_night DOUBLE NOT NULL,

    description VARCHAR(500),

    amenities VARCHAR(500),

    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'

);


-- ==========================================
-- RESERVATION TABLE
-- ==========================================

CREATE TABLE IF NOT EXISTS reservations (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    booking_id VARCHAR(50) NOT NULL UNIQUE,

    customer_id BIGINT NOT NULL,

    room_id BIGINT NOT NULL,

    check_in DATE NOT NULL,

    check_out DATE NOT NULL,

    nights INT NOT NULL,

    total_amount DOUBLE NOT NULL,

    status VARCHAR(20) NOT NULL,

    payment_method VARCHAR(20),

    transaction_id VARCHAR(100),

    CONSTRAINT fk_reservation_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(id),

    CONSTRAINT fk_reservation_room
        FOREIGN KEY (room_id)
        REFERENCES rooms(id)

);


-- ==========================================
-- SAMPLE ROOMS
-- ==========================================

INSERT IGNORE INTO rooms
(room_number, room_type, price_per_night, description, amenities, status)
VALUES
(
    '101',
    'STANDARD',
    2000,
    'Comfortable room for solo travelers and couples',
    'Wi-Fi, TV, AC, Breakfast',
    'AVAILABLE'
);


INSERT IGNORE INTO rooms
(room_number, room_type, price_per_night, description, amenities, status)
VALUES
(
    '102',
    'STANDARD',
    2000,
    'Comfortable room with modern essentials',
    'Wi-Fi, TV, AC, Breakfast',
    'AVAILABLE'
);


INSERT IGNORE INTO rooms
(room_number, room_type, price_per_night, description, amenities, status)
VALUES
(
    '201',
    'DELUXE',
    3500,
    'Spacious deluxe room with premium comfort',
    'Wi-Fi, TV, AC, Breakfast, Mini Bar',
    'AVAILABLE'
);


INSERT IGNORE INTO rooms
(room_number, room_type, price_per_night, description, amenities, status)
VALUES
(
    '202',
    'DELUXE',
    3500,
    'Elegant deluxe room for business or leisure',
    'Wi-Fi, TV, AC, Breakfast, Mini Bar',
    'AVAILABLE'
);


INSERT IGNORE INTO rooms
(room_number, room_type, price_per_night, description, amenities, status)
VALUES
(
    '301',
    'SUITE',
    5000,
    'Luxury suite with separate living space',
    'Wi-Fi, TV, AC, Breakfast, Mini Bar, Living Room',
    'AVAILABLE'
);


INSERT IGNORE INTO rooms
(room_number, room_type, price_per_night, description, amenities, status)
VALUES
(
    '302',
    'SUITE',
    5500,
    'Executive suite designed for premium stays',
    'Wi-Fi, TV, AC, Breakfast, Mini Bar, Living Room',
    'AVAILABLE'
);