-- Test data that is inserted into the test DB on application launch.
-- Set primary keys using hibernate_sequence so that the sequence is
-- at the correct number after test data insertion.
-- See https://stackoverflow.com/questions/37511719/hibernate-duplicate-key-value-violates-unique-constraint

INSERT INTO player (id, external_id, first_name, last_name) VALUES
  (101, 1, 'Pekka', 'Kana'),
  (102, 2, 'Kalle', 'Kukko'),
  (103, 3, 'Aku', 'Ankka'),
  (104, 4, 'Iines', 'Ankka');

INSERT INTO course (id, external_id, holes, name) VALUES
  (201, 1, 18, 'Frisbeegolf Laajis'),
  (202, 2, 18, 'Keljonkankaan frisbeegolfrata');

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
  (306, 2, 102, 1, 3),
  (307, 2, 102, 2, 5);

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 3;
