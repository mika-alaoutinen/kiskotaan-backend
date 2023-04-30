-- changeset mikaa:add_scores_dev
INSERT INTO score (scorecard_id, player_id, hole, score) VALUES
  (1, 1, 1, 3),
  (1, 1, 2, 3),
  (1, 1, 3, 4),
  (1, 1, 4, 5),
  (2, 2, 1, 2),
  (2, 3, 1, 3),
  (2, 2, 2, 5),
  (2, 3, 2, 4);
