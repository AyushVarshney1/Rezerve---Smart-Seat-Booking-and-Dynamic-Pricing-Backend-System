CREATE TABLE IF NOT EXISTS users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       full_name VARCHAR(255) NOT NULL,
                       phone_number VARCHAR(20) UNIQUE NOT NULL
);