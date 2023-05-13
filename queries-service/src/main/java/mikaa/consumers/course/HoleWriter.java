package mikaa.consumers.course;

import io.smallrye.mutiny.Uni;
import mikaa.HolePayload;
import mikaa.feature.course.CourseEntity;

public interface HoleWriter {

  Uni<CourseEntity> add(HolePayload payload);

  Uni<CourseEntity> update(HolePayload payload);

  Uni<Void> delete(HolePayload payload);

}
