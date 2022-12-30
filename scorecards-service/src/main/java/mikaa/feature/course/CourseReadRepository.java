package mikaa.feature.course;

import java.util.Optional;

public interface CourseReadRepository {
  Optional<CourseEntity> findByIdOptional(Long id);
}
