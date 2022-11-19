package mikaa.course;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import mikaa.dto.CourseDTO;
import mikaa.dto.CourseSummaryDTO;
import mikaa.dto.HoleDTO;
import mikaa.dto.NewCourseDTO;
import mikaa.hole.HoleEntity;

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
    newCourse.holes().stream().map(CourseService::from).forEach(entity::addHole);
    entity.persist();
    return new CourseDTO(entity);
  }

  private static HoleEntity from(HoleDTO dto) {
    return new HoleEntity(dto.number(), dto.par(), dto.distance());
  }

}
