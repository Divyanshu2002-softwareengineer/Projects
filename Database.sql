CREATE DATABASE IF NOT EXISTS farmer_support;
USE farmer_support;

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    location VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    gender VARCHAR(10) NOT NULL,
    dob DATE NOT NULL,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS crops (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    crop_name VARCHAR(100) NOT NULL,
    crop_type VARCHAR(50) NOT NULL,
    area DOUBLE NOT NULL,
    planting_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS market_prices (
    id INT PRIMARY KEY AUTO_INCREMENT,
    crop_name VARCHAR(100) NOT NULL,
    crop_type VARCHAR(50) NOT NULL,
    min_price DOUBLE NOT NULL,
    max_price DOUBLE NOT NULL,
    avg_price DOUBLE NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS weather_data (
    id INT PRIMARY KEY AUTO_INCREMENT,
    location VARCHAR(100) NOT NULL,
    temperature DOUBLE NOT NULL,
    humidity INT NOT NULL,
    wind_speed DOUBLE NOT NULL,
    weather_condition VARCHAR(50) NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample market prices data
INSERT INTO market_prices (crop_name, crop_type, min_price, max_price, avg_price) VALUES
('Wheat', 'Grains', 25.50, 28.75, 27.12),
('Rice', 'Grains', 30.00, 35.50, 32.75),
('Tomato', 'Vegetables', 15.00, 25.00, 20.00),
('Potato', 'Vegetables', 12.00, 18.00, 15.00),
('Mango', 'Fruits', 40.00, 60.00, 50.00),
('Apple', 'Fruits', 80.00, 120.00, 100.00),
('Lentils', 'Pulses', 45.00, 55.00, 50.00),
('Chickpeas', 'Pulses', 50.00, 65.00, 57.50);

-- Insert sample weather data
INSERT INTO weather_data (location, temperature, humidity, wind_speed, weather_condition) VALUES
('Delhi', 32.5, 65, 12.5, 'Sunny'),
('Mumbai', 30.0, 75, 8.0, 'Cloudy'),
('Kolkata', 28.5, 80, 10.0, 'Rainy'),
('Chennai', 35.0, 70, 15.0, 'Sunny'),
('Bangalore', 27.0, 68, 9.5, 'Cloudy');

-- Create indexes for better performance
CREATE INDEX idx_crops_username ON crops(username);
CREATE INDEX idx_market_prices_crop_type ON market_prices(crop_type);
CREATE INDEX idx_weather_data_location ON weather_data(location);

CREATE TABLE IF NOT EXISTS orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    items TEXT,
    total INT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);