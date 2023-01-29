-- Create database and tables
CREATE DATABASE players_db WITH
  ENCODING = 'UTF-8'
  TABLESPACE = pg_default
  CONNECTION LIMIT = -1;

\c players_db;

CREATE TABLE IF NOT EXISTS player (
  id          SERIAL PRIMARY KEY,
  first_name   varchar(50) NOT NULL,
  last_name    varchar(50) NOT NULL
);

-- Insert test data
INSERT INTO player (id, first_name, last_name) VALUES
  (1, 'Pekka', 'Kana'),
  (2, 'Kalle', 'Kukko'),
  (3, 'Aku', 'Ankka'),
  (4, 'Iines', 'Ankka');

-- Hibernate sequence starts from 5 because there are 24 player entries
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 5;
