package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.domain.Hole;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService {

  private final CourseRepository repository;

  void add(long courseId, Hole hole) {
    repository.findByExternalId(courseId)
        .map(course -> course.addHole(new HoleEntity(hole.number(), hole.par())))
        .ifPresent(repository::persist);
  }

  void delete(long courseId, Hole hole) {
    repository.findByExternalId(courseId)
        .map(course -> course.removeHole(hole.number()))
        .ifPresent(repository::persist);
  }

}
