-- changeset mikaa:create_table_scorecard_player
CREATE TABLE scorecard_player (
  player_id       bigint    NOT NULL,
  scorecard_id    bigint    NOT NULL,

  CONSTRAINT fk_player FOREIGN KEY(player_id) REFERENCES player(id),
  CONSTRAINT fk_scorecard FOREIGN KEY(scorecard_id) REFERENCES scorecard(id)
);
