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
        .map(CourseMapper::courseSummary)
        .toList();
  }

  Optional<CourseDTO> findOne(long id) {
    return repository.findByIdOptional(id).map(CourseMapper::course);
  }

  CourseDTO add(NewCourseDTO newCourse) {
    CourseEntity entity = CourseEntity.fromName(newCourse.name());

    newCourse.holes()
        .stream()
        .map(HoleMapper::entity)
        .forEach(entity::addHole);

    repository.persist(entity);
    return CourseMapper.course(entity);
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
