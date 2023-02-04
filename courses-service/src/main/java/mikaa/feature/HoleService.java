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
    return save(hole, HoleEventType.HOLE_ADDED);
  }

  HoleDTO update(long id, NewHoleDTO updatedHole) {
    var hole = repository.findByIdOptional(id).orElseThrow(() -> holeNotFound(id));

    hole.setDistance(updatedHole.distance());
    hole.setHoleNumber(updatedHole.number());
    hole.setPar(updatedHole.par());

    return save(hole, HoleEventType.HOLE_UPDATED);
  }

  void delete(long id) {
    repository.findByIdOptional(id)
        .map(HoleMapper::payload)
        .ifPresent(hole -> {
          repository.deleteById(id);
          producer.send(HoleEventType.HOLE_DELETED, hole);
        });
  }

  private HoleDTO save(HoleEntity hole, HoleEventType type) {
    repository.persist(hole);
    producer.send(type, HoleMapper.payload(hole));
    return HoleMapper.dto(hole);
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
