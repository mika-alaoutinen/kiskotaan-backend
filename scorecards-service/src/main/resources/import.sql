-- Test data that is inserted into the test DB on application launch.
-- Set primary keys using hibernate_sequence so that the sequence is
-- at the correct number after test data insertion.
-- See https://stackoverflow.com/questions/37511719/hibernate-duplicate-key-value-violates-unique-constraint

INSERT INTO scorecard (id) VALUES
  (1),
  (2);

-- Hibernate sequence starts from 3 because there are 2 score card entries above
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 3;

INSERT INTO course (id, scorecard_id, holes) VALUES
  (nextval('hibernate_sequence'), 1, 18),
  (nextval('hibernate_sequence'), 2, 9);

INSERT INTO player (scorecard_id, player_id) VALUES
  (1, 111),
  (2, 222),
  (2, 333);

INSERT INTO score (id, scorecard_id, player_id, hole, score) VALUES
  (nextval('hibernate_sequence'), 1, 111, 1, 3),
  (nextval('hibernate_sequence'), 1, 111, 2, 3),
  (nextval('hibernate_sequence'), 1, 111, 3, 4),
  (nextval('hibernate_sequence'), 1, 111, 4, 5),
  (nextval('hibernate_sequence'), 2, 222, 1, 2),
  (nextval('hibernate_sequence'), 2, 333, 1, 3),
  (nextval('hibernate_sequence'), 2, 222, 2, 5),
  (nextval('hibernate_sequence'), 2, 333, 2, 4);
