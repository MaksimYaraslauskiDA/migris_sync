CREATE TABLE booking (
    id UUID PRIMARY KEY,
    subscribed_at DATE NOT NULL,
    email VARCHAR(255) NOT NULL,
    service_key VARCHAR(255) NOT NULL,
    service_description VARCHAR(1000) NOT NULL,
    institution_key VARCHAR(255) NOT NULL,
    institution_description VARCHAR(1000) NOT NULL,
    creation_timestamp TIMESTAMP NOT NULL,
    update_timestamp TIMESTAMP NOT NULL
);