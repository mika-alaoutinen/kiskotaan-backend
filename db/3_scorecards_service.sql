-- Create database and tables
CREATE DATABASE scorecards_db WITH
  ENCODING = 'UTF-8'
  CONNECTION LIMIT = -1;

\c scorecards_db;

CREATE TABLE IF NOT EXISTS course (
  id            bigint        GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  external_id   bigint        UNIQUE NOT NULL,
  holes         integer       NOT NULL,
  name          varchar(50)   NOT NULL
);

CREATE TABLE IF NOT EXISTS player (
  id            bigint        GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  external_id   bigint        UNIQUE NOT NULL,
  first_name    varchar(50)   NOT NULL,
  last_name     varchar(50)   NOT NULL
);

CREATE TABLE IF NOT EXISTS score (
  id            bigint        GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  hole          integer       NOT NULL,
  score         integer       NOT NULL,
  player_id     bigint        NOT NULL,
  scorecard_id  bigint        NOT NULL
);

CREATE TABLE IF NOT EXISTS scorecard (
  id            bigint        GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  course_id     bigint        NOT NULL
);

CREATE TABLE IF NOT EXISTS scorecard_player (
  player_id     bigint        NOT NULL,
  scorecard_id  bigint        NOT NULL
);

-- Insert test data
INSERT INTO player (external_id, first_name, last_name) VALUES
  (1, 'Aku', 'Ankka'),
  (2, 'Iines', 'Ankka'),
  (3, 'Hessu', 'Hopo');

INSERT INTO course (external_id, holes, name) VALUES
  (1, 18, 'Frisbeegolf Laajis'),
  (2, 18, 'Keljonkankaan frisbeegolfrata');

INSERT INTO scorecard (course_id) VALUES
  (1),
  (2);

INSERT INTO scorecard_player (scorecard_id, player_id) VALUES
  (1, 1),
  (2, 2),
  (2, 3);

INSERT INTO score (scorecard_id, player_id, hole, score) VALUES
  (1, 1, 1, 3),
  (1, 1, 2, 3),
  (1, 1, 3, 4),
  (1, 1, 4, 5),
  (2, 2, 1, 2),
  (2, 3, 1, 3),
  (2, 2, 2, 5),
  (2, 3, 2, 4);
