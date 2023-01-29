CREATE DATABASE player_db WITH
    ENCODING = 'UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

\c player_db;

CREATE TABLE IF NOT EXISTS player (
    id           SERIAL PRIMARY KEY,
    first_name   varchar(50) NOT NULL,
    last_name    varchar(50) NOT NULL
);
