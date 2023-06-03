-- changeset mikaa:create_table_course_holes
CREATE TABLE course_holes (
  course_id     bigint    NOT NULL,
  hole_number   integer   NOT NULL,
  par           integer   NOT NULL
);
