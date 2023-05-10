package mikaa.feature.course;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

interface CourseReader {

  Uni<Course> findOne(long externalId);

  Multi<Course> findAll();

}
