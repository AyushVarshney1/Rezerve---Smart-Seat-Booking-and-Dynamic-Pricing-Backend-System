CREATE TABLE IF NOT EXISTS inventory(
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT UNIQUE NOT NULL,
    total_seats INTEGER NOT NULL,
    available_seats INTEGER NOT NULL,
    event_category VARCHAR(20) NOT NULL
);