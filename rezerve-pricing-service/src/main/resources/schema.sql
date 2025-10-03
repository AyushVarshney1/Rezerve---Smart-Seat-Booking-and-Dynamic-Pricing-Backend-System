CREATE TABLE IF NOT EXISTS event_prices (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL UNIQUE,
    event_category VARCHAR(20) NOT NULL,
    base_price DOUBLE PRECISION NOT NULL,
    current_price DOUBLE PRECISION NOT NULL,
    total_seats INTEGER NOT NULL,
    available_seats INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

