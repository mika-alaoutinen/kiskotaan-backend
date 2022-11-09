-- Connect to the database
\c player_db;

-- Copy the data from CSV to the 'player' table
COPY player(firstName, lastName)
FROM '/scripts/data.csv'
DELIMITER ','
CSV HEADER;
