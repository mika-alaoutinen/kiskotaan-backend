package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.events.course.CoursePayload;
import mikaa.events.course.CourseUpdated;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService implements CourseFinder {

  private final CourseRepository repository;

  @Override
  public CourseEntity findOrThrow(long id) {
    return repository.findByExternalId(id).orElseThrow(() -> notFound(id));
  }

  void add(CoursePayload course) {
    var entity = new CourseEntity(course.id(), course.holes().size(), course.name());
    repository.persist(entity);
  }

  void delete(CoursePayload payload) {
    repository.deleteByExternalId(payload.id());
  }

  void update(CourseUpdated course) {
    repository.findByExternalId(course.id())
        .map(entity -> {
          entity.setName(course.name());
          return entity;
        })
        .ifPresent(repository::persist);
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find course with id " + id);
  }

}
