\c testdb;

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
