DROP TABLE IF EXISTS customers;

CREATE TABLE customers
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(255)        NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);
