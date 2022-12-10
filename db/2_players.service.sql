\c testdb;

CREATE TABLE IF NOT EXISTS player (
    id          SERIAL PRIMARY KEY,
    first_name   varchar(50) NOT NULL,
    last_name    varchar(50) NOT NULL
);
