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
