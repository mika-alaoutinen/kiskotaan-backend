package mikaa.feature;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.dto.HoleDTO;
import mikaa.dto.NewHoleDTO;

@ApplicationScoped
@RequiredArgsConstructor
class HoleService {

  private final CourseRepository courseRepository;
  private final HoleRepository repository;

  Optional<HoleDTO> findOne(long id) {
    return repository.findByIdOptional(id).map(HoleMapper::dto);
  }

  Optional<HoleDTO> add(long courseId, NewHoleDTO newHole) {
    return courseRepository.findByIdOptional(courseId).map(course -> {
      HoleEntity hole = HoleMapper.entity(newHole);
      hole.setCourse(course);
      return hole;
    }).map(this::save);
  }

  Optional<HoleDTO> update(long id, NewHoleDTO updatedHole) {
    return repository.findByIdOptional(id).map(hole -> {
      hole.setDistance(updatedHole.distance());
      hole.setHoleNumber(updatedHole.number());
      hole.setPar(updatedHole.par());
      return hole;
    }).map(this::save);
  }

  void delete(long id) {
    repository.deleteById(id);
  }

  private HoleDTO save(HoleEntity hole) {
    repository.persist(hole);
    return HoleMapper.dto(hole);
  }

}
