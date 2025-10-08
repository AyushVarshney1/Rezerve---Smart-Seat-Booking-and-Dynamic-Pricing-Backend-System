CREATE TABLE IF NOT EXISTS payments (
                          id BIGSERIAL PRIMARY KEY,
                          payment_id UUID NOT NULL UNIQUE,
                          booking_id UUID NOT NULL,
                          user_id BIGINT NOT NULL,
                          user_email VARCHAR(255) NOT NULL,
                          amount DOUBLE PRECISION NOT NULL,
                          payment_status VARCHAR(50) NOT NULL,
                          created_date TIMESTAMP
);
