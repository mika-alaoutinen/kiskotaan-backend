package mikaa.feature.course;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.events.course.CoursePayload;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService implements CourseFinder {

  private final CourseRepository repository;

  @Override
  public CourseEntity findOrThrow(long id) {
    return repository.findByIdOptional(id).orElseThrow(() -> notFound(id));
  }

  void add(CoursePayload course) {
    var entity = new CourseEntity(null, course.holes().size(), course.name(), Set.of());
    repository.persist(entity);
  }

  void delete(CoursePayload course) {
  }

  void update(CoursePayload course) {
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find course with id " + id);
  }

}
