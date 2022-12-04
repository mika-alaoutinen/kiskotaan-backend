package mikaa.feature;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.dto.HoleDTO;
import mikaa.dto.NewHoleDTO;
import mikaa.kafka.KafkaProducer;
import mikaa.kafka.EventType;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService {

  private final KafkaProducer producer;
  private final CourseRepository courseRepository;
  private final HoleRepository repository;

  Optional<HoleDTO> findOne(long id) {
    return repository.findByIdOptional(id).map(HoleMapper::dto);
  }

  Optional<HoleDTO> add(long courseId, NewHoleDTO newHole) {
    return courseRepository.findByIdOptional(courseId).map(course -> {
      HoleEntity hole = HoleMapper.entity(newHole);
      course.addHole(hole);
      return hole;
    }).map(hole -> save(hole, EventType.HOLE_ADDED));
  }

  Optional<HoleDTO> update(long id, NewHoleDTO updatedHole) {
    return repository.findByIdOptional(id).map(hole -> {
      hole.setDistance(updatedHole.distance());
      hole.setHoleNumber(updatedHole.number());
      hole.setPar(updatedHole.par());
      return hole;
    }).map(hole -> save(hole, EventType.HOLE_UPDATED));
  }

  void delete(long id) {
    repository.deleteById(id);
  }

  private HoleDTO save(HoleEntity hole, EventType type) {
    repository.persist(hole);
    var dto = HoleMapper.dto(hole);
    var course = CourseMapper.course(hole.getCourse());
    producer.send(type, course);
    return dto;
  }

}
