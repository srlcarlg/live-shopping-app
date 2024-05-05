CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    live_slug VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    quantity INT NOT NULL,
    time_left INT NOT NULL,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
