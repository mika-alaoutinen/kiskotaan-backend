package mikaa.feature.course;

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
    return repository.find("courseId", id)
        .firstResultOptional()
        .orElseThrow(() -> notFound(id));
  }

  void add(CoursePayload course) {
    var entity = new CourseEntity(course.id(), course.holes().size(), course.name());
    repository.persist(entity);
  }

  void delete(CoursePayload course) {
    repository.deleteById(course.id());
  }

  void update(CoursePayload course) {
    repository.findByIdOptional(course.id())
        .map(entity -> updateName(entity, course))
        .ifPresent(repository::persist);
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find course with id " + id);
  }

  private static CourseEntity updateName(CourseEntity entity, CoursePayload updated) {
    entity.setName(updated.name());
    return entity;
  }

}
