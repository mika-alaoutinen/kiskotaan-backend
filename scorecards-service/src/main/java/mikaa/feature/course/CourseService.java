package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.kiskotaan.domain.CourseUpdated;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService implements CourseFinder {

  private final CourseRepository repository;

  @Override
  public CourseEntity findOrThrow(long id) {
    return repository.findByExternalId(id).orElseThrow(() -> notFound(id));
  }

  void add(CoursePayload course) {
    var entity = new CourseEntity(course.getId(), course.getHoles().size(), course.getName());
    repository.persist(entity);
  }

  void delete(CoursePayload payload) {
    repository.deleteByExternalId(payload.getId());
  }

  void update(CourseUpdated course) {
    repository.findByExternalId(course.getId())
        .map(entity -> {
          entity.setName(course.getName());
          return entity;
        })
        .ifPresent(repository::persist);
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find course with id " + id);
  }

}
