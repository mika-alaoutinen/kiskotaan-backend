-- changeset mikaa:create_table_course
CREATE TABLE course (
  id            bigint        GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  external_id   bigint        UNIQUE NOT NULL,
  name          varchar(50)   NOT NULL
);
