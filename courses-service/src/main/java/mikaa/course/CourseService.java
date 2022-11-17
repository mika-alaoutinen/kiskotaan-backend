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
    return entities.stream().map(CourseService::toSummary).toList();
  }

  Optional<CourseDTO> findOne(long id) {
    return Optional.empty();
  }

  private static CourseSummaryDTO toSummary(CourseEntity entity) {
    var coursePar = entity.holes.stream().mapToInt(h -> h.par).sum();
    return new CourseSummaryDTO(entity.id, entity.name, entity.holes.size(), coursePar);
  }
}
