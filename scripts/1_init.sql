-- Create a new database where the data from CSV will be saved
CREATE DATABASE player_db WITH
    ENCODING = 'UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Connect to the new database
\c player_db;

-- Create a new table
CREATE TABLE IF NOT EXISTS player (
    id          SERIAL PRIMARY KEY,
    firstName   varchar(50) NOT NULL,
    lastName    varchar(50) NOT NULL
);
