package mikaa.queries.course;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import mikaa.feature.course.Course;

public interface CourseReader {

  Uni<Course> findOne(long externalId);

  Multi<Course> findAll();

}
