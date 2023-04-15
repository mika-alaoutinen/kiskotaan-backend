-- changeset mikaa:create_database
CREATE DATABASE player_db WITH
  ENCODING = 'UTF-8'
  TABLESPACE = pg_default
  CONNECTION LIMIT = -1;
  