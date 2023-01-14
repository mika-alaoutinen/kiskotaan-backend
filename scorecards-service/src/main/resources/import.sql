-- Test data that is inserted into the test DB on application launch.
-- Set primary keys using hibernate_sequence so that the sequence is
-- at the correct number after test data insertion.
-- See https://stackoverflow.com/questions/37511719/hibernate-duplicate-key-value-violates-unique-constraint

INSERT INTO scorecard (id) VALUES
  (nextval('hibernate_sequence')),
  (nextval('hibernate_sequence'));

INSERT INTO player (id, first_name, last_name) VALUES
  ((nextval('hibernate_sequence')), 'Pekka', 'Kana'),
  ((nextval('hibernate_sequence')), 'Kalle', 'Kukko'),
  ((nextval('hibernate_sequence')), 'Riki', 'Sorsa');

INSERT INTO scorecard_player (scorecard_id, player_id) VALUES
  (1, 3),
  (2, 4),
  (2, 5);

INSERT INTO course (id, scorecard_id, holes, name) VALUES
  (nextval('hibernate_sequence'), 1, 18, 'Frisbeegolf Laajis'),
  (nextval('hibernate_sequence'), 2, 9, 'Keljonkankaan frisbeegolfrata');

INSERT INTO score (id, scorecard_id, player_id, hole, score) VALUES
  (nextval('hibernate_sequence'), 1, 3, 1, 3),
  (nextval('hibernate_sequence'), 1, 3, 2, 3),
  (nextval('hibernate_sequence'), 1, 3, 3, 4),
  (nextval('hibernate_sequence'), 1, 3, 4, 5),
  (nextval('hibernate_sequence'), 2, 4, 1, 2),
  (nextval('hibernate_sequence'), 2, 4, 1, 3),
  (nextval('hibernate_sequence'), 2, 4, 2, 5);
