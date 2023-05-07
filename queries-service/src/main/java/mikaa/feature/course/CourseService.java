package mikaa.feature.course;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService {

  private final CourseRepository repository;

  Uni<Course> getCourse(long externalId) {
    return repository.findByExternalId(externalId)
        .replaceIfNullWith(() -> {
          throw notFound(externalId);
        });
  }

  Multi<Course> getCourses() {
    return repository.streamAll();
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find course with ID " + id);
  }
}
