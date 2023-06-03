package mikaa.feature.course;

import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.kiskotaan.domain.CourseUpdated;
import mikaa.kiskotaan.domain.Hole;

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
        .collect(Collectors.toMap(Hole::getNumber, Hole::getPar));

    var entity = new CourseEntity(course.getId(), holes, course.getName());
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
