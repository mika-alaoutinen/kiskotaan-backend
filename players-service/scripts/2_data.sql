\c player_db;

COPY player(first_name, last_name)
FROM '/data/data.csv'
DELIMITER ','
CSV HEADER;
