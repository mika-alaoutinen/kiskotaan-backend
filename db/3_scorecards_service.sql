-- Create database and tables
CREATE DATABASE scorecards_db WITH
ENCODING = 'UTF-8'
  TABLESPACE = pg_default
  CONNECTION LIMIT = -1;

\c scorecards_db;

CREATE TABLE IF NOT EXISTS course (
  id          bigint PRIMARY KEY,
  name        varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS player (
  id          bigint PRIMARY KEY,
  first_name  varchar(50) NOT NULL,
  last_name   varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS score (
  id            bigint PRIMARY KEY,
  hole          integer NOT NULL,
  score         integer NOT NULL,
  player_id     bigint,
  scorecard_id  bigint
);

CREATE TABLE IF NOT EXISTS scorecard (
  id          bigint PRIMARY KEY,
  course_id   bigint
);

CREATE TABLE IF NOT EXISTS scorecard_player (
  player_id     bigint,
  scorecard_id  bigint
);

-- Insert test data
INSERT INTO player (id, first_name, last_name) VALUES
  (101, 'Pekka', 'Kana'),
  (102, 'Kalle', 'Kukko'),
  (103, 'Aku', 'Ankka'),
  (104, 'Iines', 'Ankka');

INSERT INTO course (id, course_id, holes, name) VALUES
  (201, 37, 18, 'Frisbeegolf Laajis'),
  (202, 38, 18, 'Keljonkankaan frisbeegolfrata');

INSERT INTO scorecard (id, course_id) VALUES
  (1, 201),
  (2, 202);

INSERT INTO scorecard_player (scorecard_id, player_id) VALUES
  (1, 101),
  (2, 102),
  (2, 103);

INSERT INTO score (id, scorecard_id, player_id, hole, score) VALUES
  (301, 1, 101, 1, 3),
  (302, 1, 101, 2, 3),
  (303, 1, 101, 3, 4),
  (304, 1, 101, 4, 5),

  (305, 2, 102, 1, 2),
  (306, 2, 103, 1, 3),
  (307, 2, 102, 2, 5),
  (308, 2, 103, 2, 4);
