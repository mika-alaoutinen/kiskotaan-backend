-- Create database and tables
CREATE DATABASE courses_db WITH
  ENCODING = 'UTF-8'
  TABLESPACE = pg_default
  CONNECTION LIMIT = -1;

\c courses_db;

CREATE TABLE IF NOT EXISTS course (
  id      bigint PRIMARY KEY,
  name    varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS hole (
  id          bigint PRIMARY KEY,
  course_id   bigint NOT NULL,
  hole_number integer NOT NULL,
  par         integer NOT NULL,
  distance    integer NOT NULL,
  CONSTRAINT fk_course
    FOREIGN KEY(course_id)
      REFERENCES course(id)
);

-- Insert test data
INSERT INTO course (id, name) VALUES
  (1, 'Frisbeegolf Laajis'),
  (2, 'Keljonkankaan frisbeegolfrata');

INSERT INTO hole (id, course_id, hole_number, par, distance) VALUES
  (3, 1, 1, 3, 107),
  (4, 1, 2, 3, 127),
  (5, 1, 3, 3, 107),
  (6, 1, 4, 3, 49),
  (7, 1, 5, 3, 97),
  (8, 1, 6, 3, 100),
  (9, 1, 7, 3, 62),
  (10, 1, 8, 3, 90),
  (11, 1, 9, 4, 172),
  (12, 1, 10, 3, 100),
  (13, 1, 11, 4, 165),
  (14, 1, 12, 3, 80),
  (15, 1, 13, 3, 89),
  (16, 1, 14, 3, 76),
  (17, 1, 15, 4, 140),
  (18, 1, 16, 3, 107),
  (19, 1, 17, 4, 183),
  (20, 1, 18, 3, 164),
  (21, 2, 1, 3, 92),
  (22, 2, 2, 4, 158),
  (23, 2, 3, 3, 95),
  (24, 2, 4, 3, 80),
  (25, 2, 5, 3, 90),
  (26, 2, 6, 3, 94),
  (27, 2, 7, 4, 141),
  (28, 2, 8, 3, 98),
  (29, 2, 9, 4, 12),
  (30, 2, 10, 3, 90),
  (31, 2, 11, 4, 116),
  (32, 2, 12, 3, 90),
  (33, 2, 13, 4, 154),
  (34, 2, 14, 4, 12),
  (35, 2, 15, 4, 177),
  (36, 2, 16, 3, 91),
  (37, 2, 17, 5, 251),
  (38, 2, 18, 3, 98);

-- Hibernate sequence starts from 39 because there are 38 course and hole entries above
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 39;
