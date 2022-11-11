-- Connect to the database
\c player_db;

-- Copy the data from CSV to the 'player' table
COPY player(first_name, last_name)
FROM '/data/data.csv'
DELIMITER ','
CSV HEADER;
