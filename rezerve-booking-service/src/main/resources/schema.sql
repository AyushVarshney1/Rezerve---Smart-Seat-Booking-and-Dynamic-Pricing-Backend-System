CREATE TABLE IF NOT EXISTS bookings (
    id BIGSERIAL PRIMARY KEY,
    booking_id UUID NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    event_category VARCHAR(20) NOT NULL,
    total_tickets INTEGER NOT NULL,
    total_price DOUBLE PRECISION NOT NULL,
    booking_status VARCHAR(20) NOT NULL,
    created_date TIMESTAMP NOT NULL
);