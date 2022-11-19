package mikaa.feature;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.dto.CourseDTO;
import mikaa.dto.CourseSummaryDTO;
import mikaa.dto.NewCourseDTO;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService {
  
  private final CourseRepository repository;

  List<CourseSummaryDTO> findAll() {
    return repository.listAll()
        .stream()
        .map(CourseSummaryDTO::new)
        .toList();
  }

  Optional<CourseDTO> findOne(long id) {
    return repository.findByIdOptional(id).map(CourseDTO::new);
  }

  CourseDTO add(NewCourseDTO newCourse) {
    CourseEntity entity = new CourseEntity(newCourse.name());
    newCourse.holes().stream().map(HoleEntity::new).forEach(entity::addHole);
    repository.persist(entity);
    return new CourseDTO(entity);
  }

  Optional<String> updateCourseName(long id, String name) {
    var maybeCourse = repository.findByIdOptional(id);

    maybeCourse.ifPresent(course -> {
      course.setName(name);
      repository.persist(course);
    });

    return maybeCourse.map(CourseEntity::getName);
  }

  void delete(long id) {
    repository.deleteById(id);
  }

}
