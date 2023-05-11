package mikaa.queries.course;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import mikaa.feature.course.CourseEntity;

public interface CourseReader {

  Uni<CourseEntity> findOne(long externalId);

  Multi<CourseEntity> findAll();

}
