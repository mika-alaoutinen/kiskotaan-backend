-- Connect to the database
\c course_db;

-- Copy the data from CSV to the 'course' table
COPY course(id, name)
FROM '/data/courses.csv'
DELIMITER ','
CSV HEADER;

-- Copy the data from CSV to the 'hole' table
COPY hole(course_id, hole_number, par, distance)
FROM '/data/holes.csv'
DELIMITER ','
CSV HEADER;
