package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.HolePayload;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService {

  private final CourseRepository repository;

  void add(HolePayload hole) {
    repository.findByIdOptional(hole.getCourseId())
        .map(course -> course.addHole(hole.getPar()))
        .ifPresent(repository::persist);
  }

  void delete(HolePayload hole) {
    repository.findByIdOptional(hole.getCourseId())
        .map(course -> course.removeHole(hole.getPar()))
        .ifPresent(repository::persist);
  }

}
