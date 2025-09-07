CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       phone_number BIGINT UNIQUE NOT NULL
);