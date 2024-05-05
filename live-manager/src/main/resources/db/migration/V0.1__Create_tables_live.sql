
CREATE SEQUENCE live_id_seq START 1;

CREATE TABLE live (
    id BIGINT NOT NULL DEFAULT NEXTVAL('live_id_seq'),
    slug VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    password VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
