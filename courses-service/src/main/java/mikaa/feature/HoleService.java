package mikaa.feature;

import java.util.Collections;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.HolePayload;
import mikaa.producers.holes.HoleProducer;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService {

  private final CourseFinder courseFinder;
  private final HoleProducer producer;
  private final HoleRepository repository;

  List<HoleEntity> findCourseHoles(long courseId) {
    return Collections.emptyList();
  }

  HoleEntity findOne(long courseId, int holeNumber) {
    return repository.findByIdOptional(courseId).orElseThrow(() -> holeNotFound(courseId));
  }

  HoleEntity add(long courseId, HoleEntity newHole) {
    var course = courseFinder.findCourse(courseId).orElseThrow(() -> courseNotFound(courseId));

    HoleValidator.validateUniqueHoleNumber(newHole.getHoleNumber(), course);
    course.addHole(newHole);

    repository.persist(newHole);
    producer.holeAdded(payload(newHole));

    return newHole;
  }

  HoleEntity update(long courseId, int holeNumber, HoleEntity updatedHole) {
    var hole = repository.findByIdOptional(courseId).orElseThrow(() -> holeNotFound(courseId));

    hole.setDistance(updatedHole.getDistance());
    hole.setHoleNumber(updatedHole.getHoleNumber());
    hole.setPar(updatedHole.getPar());

    producer.holeUpdated(payload(hole));

    return hole;
  }

  void delete(long courseId, int holeNumber) {
    repository.findByIdOptional(courseId)
        .map(HoleService::payload)
        .ifPresent(payload -> {
          repository.deleteById(courseId);
          producer.holeDeleted(payload);
        });
  }

  private static NotFoundException courseNotFound(long id) {
    String msg = "Could not find course with id " + id;
    return new NotFoundException(msg);
  }

  private static NotFoundException holeNotFound(long id) {
    String msg = "Could not find hole with id " + id;
    return new NotFoundException(msg);
  }

  private static HolePayload payload(HoleEntity entity) {
    return new HolePayload(
        entity.getId(),
        entity.getCourse().getId(),
        entity.getHoleNumber(),
        entity.getPar(),
        entity.getDistance());
  }

}
