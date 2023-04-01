INSERT INTO player (external_id, first_name, last_name) VALUES
  (101, 'Pekka', 'Kana'),
  (102, 'Kalle', 'Kukko'),
  (103, 'Aku', 'Ankka'),
  (104, 'Iines', 'Ankka');

INSERT INTO course (external_id, holes, name) VALUES
  (201, 18, 'Frisbeegolf Laajis'),
  (202, 18, 'Keljonkankaan frisbeegolfrata');

INSERT INTO scorecard (course_id) VALUES
  (1),
  (2);

INSERT INTO scorecard_player (scorecard_id, player_id) VALUES
  (1, 1),
  (2, 2),
  (2, 3);

INSERT INTO score (scorecard_id, player_id, hole, score) VALUES
  (1, 1, 1, 3),
  (1, 1, 2, 3),
  (1, 1, 3, 4),
  (1, 1, 4, 5),
  (2, 2, 1, 2),
  (2, 2, 1, 3),
  (2, 2, 2, 5);
