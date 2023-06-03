-- changeset mikaa:create_table_course_holes
CREATE TABLE course_holes (
  course_id     bigint    NOT NULL,
  number        integer   NOT NULL,
  par           integer   NOT NULL,

  CONSTRAINT fk_course FOREIGN KEY(course_id) REFERENCES course(id)
);
