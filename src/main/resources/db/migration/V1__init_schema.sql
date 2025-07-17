CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS provider (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    base_url VARCHAR(255),
    validity_days INT NOT NULL DEFAULT 30,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS ip_lookup_result (
    request_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ip_address VARCHAR(45) NOT NULL UNIQUE,
    continent VARCHAR(100),
    country VARCHAR(100),
    region VARCHAR(100),
    city VARCHAR(100),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    fetched_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    provider_id INT NOT NULL,
    CONSTRAINT fk_provider
        FOREIGN KEY (provider_id)
        REFERENCES provider(id)
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS event (
    id SERIAL PRIMARY KEY,
    request_id UUID,
    data JSONB NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_ip_lookup_ip ON ip_lookup_result (ip_address);
CREATE INDEX IF NOT EXISTS idx_ip_lookup_provider ON ip_lookup_result (provider_id);
