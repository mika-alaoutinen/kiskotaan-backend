package mikaa.feature;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.dto.HoleDTO;
import mikaa.dto.NewHoleDTO;
import mikaa.kafka.holes.HoleEventType;
import mikaa.kafka.holes.HoleProducer;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService {

  private final HoleProducer producer;
  private final CourseRepository courseRepository;
  private final HoleRepository repository;

  HoleDTO findOne(long id) {
    return repository.findByIdOptional(id)
        .map(HoleMapper::dto)
        .orElseThrow(() -> holeNotFound(id));
  }

  HoleDTO add(long courseId, NewHoleDTO newHole) {
    var course = courseRepository.findByIdOptional(courseId).orElseThrow(() -> courseNotFound(courseId));

    HoleEntity hole = HoleMapper.entity(newHole);
    course.addHole(hole);

    repository.persist(hole);
    producer.send(HoleEventType.HOLE_ADDED, HoleMapper.payload(hole));
    return HoleMapper.dto(hole);
  }

  HoleDTO update(long id, NewHoleDTO updatedHole) {
    var hole = repository.findByIdOptional(id).orElseThrow(() -> holeNotFound(id));

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

  private static NotFoundException courseNotFound(long id) {
    String msg = "Could not find course with id " + id;
    return new NotFoundException(msg);
  }

  private static NotFoundException holeNotFound(long id) {
    String msg = "Could not find hole with id " + id;
    return new NotFoundException(msg);
  }

}
