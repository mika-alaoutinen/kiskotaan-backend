package mikaa.feature.course;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import mikaa.consumers.course.CourseWriter;
import mikaa.queries.course.CourseReader;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService implements CourseReader, CourseWriter {

  private final CourseRepository repository;

  @Override
  public Uni<Course> findOne(long externalId) {
    return repository.findByExternalId(externalId)
        .replaceIfNullWith(() -> {
          throw notFound(externalId);
        });
  }

  @Override
  public Multi<Course> findAll() {
    return repository.streamAll();
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find course with ID " + id);
  }

  @Override
  public void addOne(Course course) {
  }
}
