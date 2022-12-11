\c course_db;

COPY course(id, name)
FROM '/data/courses.csv'
DELIMITER ','
CSV HEADER;

COPY hole(id, course_id, hole_number, par, distance)
FROM '/data/holes.csv'
DELIMITER ','
CSV HEADER;
