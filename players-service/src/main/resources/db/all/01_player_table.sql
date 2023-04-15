-- changeset mikaa:create_table_player
CREATE TABLE player (
  id           SERIAL PRIMARY KEY,
  first_name   varchar(50) NOT NULL,
  last_name    varchar(50) NOT NULL
);
