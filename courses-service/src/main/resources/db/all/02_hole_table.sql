-- changeset mikaa:create_table_hole
CREATE TABLE hole (
  id              bigint     GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  course_id       bigint     NOT NULL,
  number          integer    NOT NULL,
  par             integer    NOT NULL,
  distance        integer    NOT NULL,
  CONSTRAINT fk_course FOREIGN KEY(course_id) REFERENCES course(id)
);
