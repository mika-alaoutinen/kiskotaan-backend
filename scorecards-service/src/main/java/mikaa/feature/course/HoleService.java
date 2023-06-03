package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.HolePayload;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService {

  private final CourseRepository repository;

  void add(HolePayload hole) {
    repository.findByExternalId(hole.getCourseId())
        .map(course -> course.addHole(new HoleEntity(hole.getNumber(), hole.getPar())))
        .ifPresent(repository::persist);
  }

  void delete(HolePayload hole) {
    repository.findByExternalId(hole.getCourseId())
        .map(course -> course.removeHole(hole.getNumber()))
        .ifPresent(repository::persist);
  }

}
