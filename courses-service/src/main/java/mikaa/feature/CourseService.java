package mikaa.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import mikaa.dto.CourseDTO;
import mikaa.dto.CourseSummaryDTO;
import mikaa.dto.NewCourseDTO;

@ApplicationScoped
class CourseService {

  List<CourseSummaryDTO> findAll() {
    List<CourseEntity> entities = CourseEntity.listAll();
    return entities.stream().map(CourseSummaryDTO::new).toList();
  }

  Optional<CourseDTO> findOne(long id) {
    Optional<CourseEntity> courseOpt = CourseEntity.findByIdOptional(id);
    return courseOpt.map(CourseDTO::new);
  }

  CourseDTO add(NewCourseDTO newCourse) {
    CourseEntity entity = new CourseEntity(newCourse.name(), new ArrayList<>());
    newCourse.holes().stream().map(HoleEntity::new).forEach(entity::addHole);
    entity.persist();
    return new CourseDTO(entity);
  }

  Optional<String> updateCourseName(long id, String name) {
    Optional<CourseEntity> entity = CourseEntity.findByIdOptional(id);
    return entity.map(e -> {
      e.name = name;
      return name;
    });
  }

}
