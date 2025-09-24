CREATE TYPE booking_status_type AS ENUM ('PENDING', 'CONFIRMED', 'CANCELLED');
CREATE TYPE event_category_type AS ENUM ('CONCERT','MOVIE','FLIGHT','TRAIN','BUS');

CREATE TABLE IF NOT EXISTS bookings (
    id BIGSERIAL PRIMARY KEY,
    booking_id UUID NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    event_category event_category_type NOT NULL,
    total_tickets INTEGER NOT NULL,
    total_price DOUBLE PRECISION NOT NULL,
    booking_status booking_status_type NOT NULL,
    created_date TIMESTAMP NOT NULL
);