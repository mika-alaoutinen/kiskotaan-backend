package mikaa.feature;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.dto.HoleDTO;
import mikaa.dto.NewHoleDTO;
import mikaa.kafka.holes.HoleEventType;
import mikaa.kafka.holes.HoleProducer;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService {

  private final CourseFinder courseFinder;
  private final HoleProducer producer;
  private final HoleRepository repository;

  HoleDTO findOne(long id) {
    return repository.findByIdOptional(id)
        .map(HoleMapper::dto)
        .orElseThrow(() -> holeNotFound(id));
  }

  HoleEntity add(long courseId, HoleEntity newHole) {
    var course = courseFinder.findOne(courseId);

    HoleValidator.validateUniqueHoleNumber(newHole.getHoleNumber(), course);
    course.addHole(newHole);

    repository.persist(newHole);
    producer.send(HoleEventType.HOLE_ADDED, HoleMapper.payload(newHole));

    return newHole;
  }

  HoleDTO update(long id, NewHoleDTO updatedHole) {
    var hole = repository.findByIdOptional(id).orElseThrow(() -> holeNotFound(id));

    HoleValidator.validateUniqueHoleNumber(updatedHole.number(), hole.getCourse());

    hole.setDistance(updatedHole.distance());
    hole.setHoleNumber(updatedHole.number());
    hole.setPar(updatedHole.par());

    producer.send(HoleEventType.HOLE_UPDATED, HoleMapper.payload(hole));
    return HoleMapper.dto(hole);
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .map(HoleMapper::payload)
        .ifPresent(hole -> {
          repository.deleteById(id);
          producer.send(HoleEventType.HOLE_DELETED, hole);
        });
  }

  private static NotFoundException holeNotFound(long id) {
    String msg = "Could not find hole with id " + id;
    return new NotFoundException(msg);
  }

}
