package mikaa.feature;

import java.util.List;

import mikaa.dto.CourseDTO;
import mikaa.dto.CourseNameDTO;
import mikaa.dto.CourseSummary;
import mikaa.dto.HoleDTO;

interface CourseMapper {

  static CourseDTO course(CourseEntity entity) {
    return new CourseDTO(entity.getId(), entity.getName(), holes(entity));
  }

  private static List<HoleDTO> holes(CourseEntity entity) {
    return entity.getHoles().stream().map(HoleMapper::dto).toList();
  }

  static CourseSummary courseSummary(CourseEntity entity) {
    return new CourseSummary(
        entity.getId(),
        entity.getName(),
        courseHoleCount(entity),
        coursePar(entity));
  }

  private static int courseHoleCount(CourseEntity entity) {
    return entity.getHoles().size();
  }

  private static int coursePar(CourseEntity entity) {
    return entity.getHoles().stream().mapToInt(HoleEntity::getPar).sum();
  }

  static CourseNameDTO courseName(CourseEntity entity) {
    return new CourseNameDTO(entity.getId(), entity.getName());
  }

}
