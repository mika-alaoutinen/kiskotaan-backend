package mikaa.course;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import mikaa.dto.CourseDTO;
import mikaa.dto.CourseSummaryDTO;
import mikaa.dto.HoleDTO;

@ApplicationScoped
class CourseService {

  List<CourseSummaryDTO> findAll() {
    List<CourseEntity> entities = CourseEntity.listAll();
    return entities.stream().map(CourseService::toSummary).toList();
  }

  Optional<CourseDTO> findOne(long id) {
    Optional<CourseEntity> courseOpt = CourseEntity.findByIdOptional(id);
    return courseOpt.map(course -> {
      var holes = course.holes.stream().map(HoleDTO::new).toList();
      return new CourseDTO(course.id, course.name, holes);
    });
  }

  private static CourseSummaryDTO toSummary(CourseEntity entity) {
    var coursePar = entity.holes.stream().mapToInt(h -> h.par).sum();
    return new CourseSummaryDTO(entity.id, entity.name, entity.holes.size(), coursePar);
  }
}
