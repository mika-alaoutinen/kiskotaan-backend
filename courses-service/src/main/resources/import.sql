-- Test data that is inserted into the test DB on application launch.
-- Set primary keys using hibernate_sequence so that the sequence is
-- at the correct number after test data insertion.
-- See https://stackoverflow.com/questions/37511719/hibernate-duplicate-key-value-violates-unique-constraint

INSERT INTO course (id, name) VALUES
  (nextval('hibernate_sequence'), 'Frisbeegolf Laajis'),
  (nextval('hibernate_sequence'), 'Keljonkankaan frisbeegolfrata');

INSERT INTO hole (id, course_id, hole_number, par, distance) VALUES
  (nextval('hibernate_sequence'), 1, 1, 3, 107),
  (nextval('hibernate_sequence'), 1, 2, 3, 127),
  (nextval('hibernate_sequence'), 1, 3, 3, 107),
  (nextval('hibernate_sequence'), 1, 4, 3, 49),
  (nextval('hibernate_sequence'), 1, 5, 3, 97),
  (nextval('hibernate_sequence'), 1, 6, 3, 100),
  (nextval('hibernate_sequence'), 1, 7, 3, 62),
  (nextval('hibernate_sequence'), 1, 8, 3, 90),
  (nextval('hibernate_sequence'), 1, 9, 4, 172),
  (nextval('hibernate_sequence'), 1, 10, 3, 100),
  (nextval('hibernate_sequence'), 1, 11, 4, 165),
  (nextval('hibernate_sequence'), 1, 12, 3, 80),
  (nextval('hibernate_sequence'), 1, 13, 3, 89),
  (nextval('hibernate_sequence'), 1, 14, 3, 76),
  (nextval('hibernate_sequence'), 1, 15, 4, 140),
  (nextval('hibernate_sequence'), 1, 16, 3, 107),
  (nextval('hibernate_sequence'), 1, 17, 4, 183),
  (nextval('hibernate_sequence'), 1, 18, 3, 164),
  (nextval('hibernate_sequence'), 2, 1, 3, 92),
  (nextval('hibernate_sequence'), 2, 2, 4, 158),
  (nextval('hibernate_sequence'), 2, 3, 3, 95),
  (nextval('hibernate_sequence'), 2, 4, 3, 80),
  (nextval('hibernate_sequence'), 2, 5, 3, 90),
  (nextval('hibernate_sequence'), 2, 6, 3, 94),
  (nextval('hibernate_sequence'), 2, 7, 4, 141),
  (nextval('hibernate_sequence'), 2, 8, 3, 98),
  (nextval('hibernate_sequence'), 2, 9, 4, 138),
  (nextval('hibernate_sequence'), 2, 10, 3, 90),
  (nextval('hibernate_sequence'), 2, 11, 4, 116),
  (nextval('hibernate_sequence'), 2, 12, 3, 90),
  (nextval('hibernate_sequence'), 2, 13, 4, 154),
  (nextval('hibernate_sequence'), 2, 14, 4, 138),
  (nextval('hibernate_sequence'), 2, 15, 4, 177),
  (nextval('hibernate_sequence'), 2, 16, 3, 91),
  (nextval('hibernate_sequence'), 2, 17, 5, 251),
  (nextval('hibernate_sequence'), 2, 18, 3, 98);
