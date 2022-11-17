package mikaa.course;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import mikaa.dto.CourseDTO;
import mikaa.dto.CourseSummaryDTO;

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

}
