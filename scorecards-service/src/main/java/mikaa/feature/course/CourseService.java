package mikaa.feature.course;

import java.util.Collection;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.domain.Course;
import mikaa.domain.Hole;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService implements CourseFinder {

  private final CourseRepository repository;

  @Override
  public CourseEntity findOrThrow(long id) {
    return repository.findByExternalId(id).orElseThrow(() -> notFound(id));
  }

  void add(Course course) {
    var holes = course.holes()
        .stream()
        .map(h -> new HoleEntity(h.number(), h.par()))
        .toList();

    var entity = new CourseEntity(course.id(), holes, course.name());
    repository.persist(entity);
  }

  void delete(Course payload) {
    repository.deleteByExternalId(payload.id());
  }

  void update(Course course) {
    repository.findByExternalId(course.id())
        .map(entity -> {
          entity.setName(course.name());
          entity.setHoles(mapHoles(course.holes()));
          return entity;
        })
        .ifPresent(repository::persist);
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find course with id " + id);
  }

  private static List<HoleEntity> mapHoles(Collection<Hole> holes) {
    return holes.stream().map(h -> new HoleEntity(h.number(), h.par())).toList();
  }

}
