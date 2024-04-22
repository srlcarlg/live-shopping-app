CREATE TABLE live_statement (
    id SERIAL PRIMARY KEY,
    live_slug VARCHAR(255) NOT NULL,
    total DOUBLE PRECISION NOT NULL
);

CREATE TABLE transaction (
    id SERIAL PRIMARY KEY,
    uuid UUID NOT NULL,
    live_slug VARCHAR(255) NOT NULL,
    product_id BIGINT,
    amount DOUBLE PRECISION NOT NULL,
    credit_card_number BIGINT,
    created_at TIMESTAMP NOT NULL
);

