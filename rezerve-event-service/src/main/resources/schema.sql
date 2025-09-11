CREATE TABLE IF NOT EXISTS events (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        category VARCHAR(50) NOT NULL,
                        description VARCHAR(500),

                        from_location VARCHAR(255),
                        to_location VARCHAR(255),

                        venue_location VARCHAR(255),

                        start_time TIMESTAMP NOT NULL,
                        end_time TIMESTAMP NOT NULL,

                        total_seats INT NOT NULL,
                        price DOUBLE PRECISION NOT NULL,

                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
