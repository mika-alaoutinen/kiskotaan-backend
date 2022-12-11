-- Create database and tables
CREATE DATABASE courses_db WITH
    ENCODING = 'UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

\c courses_db;

CREATE TABLE IF NOT EXISTS course (
    id          bigint PRIMARY KEY,
    name        varchar(50) NOT NULL
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
    (37, 'Frisbeegolf Laajis'),
    (38, 'Keljonkankaan frisbeegolfrata');

INSERT INTO hole (id, course_id, hole_number, par, distance) VALUES
    (1, 37, 1, 3, 107),
    (2, 37, 2, 3, 127),
    (3, 37, 3, 3, 107),
    (4, 37, 4, 3, 49),
    (5, 37, 5, 3, 97),
    (6, 37, 6, 3, 100),
    (7, 37, 7, 3, 62),
    (8, 37, 8, 3, 90),
    (9, 37, 9, 4, 172),
    (10, 37, 10, 3, 100),
    (11, 37, 11, 4, 165),
    (12, 37, 12, 3, 80),
    (13, 37, 13, 3, 89),
    (14, 37, 14, 3, 76),
    (15, 37, 15, 4, 140),
    (16, 37, 16, 3, 107),
    (17, 37, 17, 4, 183),
    (18, 37, 18, 3, 164),
    (19, 38, 1, 3, 92),
    (20, 38, 2, 4, 158),
    (21, 38, 3, 3, 95),
    (22, 38, 4, 3, 80),
    (23, 38, 5, 3, 90),
    (24, 38, 6, 3, 94),
    (25, 38, 7, 4, 141),
    (26, 38, 8, 3, 98),
    (27, 38, 9, 4, 138),
    (28, 38, 10, 3, 90),
    (29, 38, 11, 4, 116),
    (30, 38, 12, 3, 90),
    (31, 38, 13, 4, 154),
    (32, 38, 14, 4, 138),
    (33, 38, 15, 4, 177),
    (34, 38, 16, 3, 91),
    (35, 38, 17, 5, 251),
    (36, 38, 18, 3, 98);

-- Hibernate sequence starts from 39 because there are 38 course and hole entries above
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 39;
