package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.HolePayload;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService {

  private final CourseRepository repository;

  void add(HolePayload hole) {
    repository.findByIdOptional(hole.courseId())
        .map(CourseEntity::incrementHoleCount)
        .ifPresent(repository::persist);
  }

  void delete(HolePayload hole) {
    repository.findByIdOptional(hole.courseId())
        .map(CourseEntity::decrementHoleCount)
        .ifPresent(repository::persist);
  }

}
