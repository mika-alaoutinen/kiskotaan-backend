package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.courses.CoursePayload;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService implements CourseFinder {

  private final CourseRepository repository;

  @Override
  public CourseEntity findOrThrow(long id) {
    return repository.findByExternalId(id).orElseThrow(() -> notFound(id));
  }

  void add(CoursePayload course) {
    var holes = course.getHoles()
        .stream()
        .map(h -> new HoleEntity(h.getNumber(), h.getPar()))
        .toList();

    var entity = new CourseEntity(course.getId(), holes, course.getName());
    repository.persist(entity);
  }

  void delete(CoursePayload payload) {
    repository.deleteByExternalId(payload.getId());
  }

  void update(CoursePayload course) {
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
