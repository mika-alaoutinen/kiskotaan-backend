package mikaa.consumers.course;

import io.smallrye.mutiny.Uni;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.kiskotaan.domain.CourseUpdated;
import mikaa.feature.course.CourseEntity;

public interface CourseWriter {

  Uni<CourseEntity> add(CoursePayload payload);

  Uni<CourseEntity> update(CourseUpdated payload);

  Uni<Void> delete(CoursePayload payload);

}
