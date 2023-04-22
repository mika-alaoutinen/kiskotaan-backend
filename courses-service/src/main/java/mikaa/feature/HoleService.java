package mikaa.feature;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.events.holes.HolePayload;
import mikaa.events.holes.HoleProducer;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService {

  private final CourseService courseService;
  private final HoleProducer producer;
  private final HoleRepository repository;

  HoleEntity findOne(long id) {
    return repository.findByIdOptional(id).orElseThrow(() -> holeNotFound(id));
  }

  HoleEntity add(long courseId, HoleEntity newHole) {
    var course = courseService.findOne(courseId);

    HoleValidator.validateUniqueHoleNumber(newHole.getHoleNumber(), course);
    course.addHole(newHole);

    repository.persist(newHole);
    producer.holeAdded(payload(newHole));

    return newHole;
  }

  HoleEntity update(long id, HoleEntity updatedHole) {
    var hole = repository.findByIdOptional(id).orElseThrow(() -> holeNotFound(id));

    HoleValidator.validateUniqueHoleNumber(updatedHole.getHoleNumber(), hole.getCourse());

    hole.setDistance(updatedHole.getDistance());
    hole.setHoleNumber(updatedHole.getHoleNumber());
    hole.setPar(updatedHole.getPar());

    producer.holeUpdated(payload(hole));

    return hole;
  }

  void delete(long id) {
    repository.findByIdOptional(id).ifPresent(_h -> {
      repository.deleteById(id);
      producer.holeDeleted(id);
    });
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
