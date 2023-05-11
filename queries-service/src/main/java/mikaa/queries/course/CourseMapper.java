package mikaa.queries.course;

import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.HoleEntity;
import mikaa.queries.dto.CourseDTO;
import mikaa.queries.dto.CourseSummaryDTO;
import mikaa.queries.dto.HoleDTO;

interface CourseMapper {

  static CourseDTO toCourse(CourseEntity course) {
    var holes = course.getHoles().stream().map(CourseMapper::toHole).toList();

    return new CourseDTO(
        course.getExternalId(),
        course.getName(),
        calculateCoursePar(course),
        holes);
  }

  static CourseSummaryDTO toSummary(CourseEntity course) {
    return new CourseSummaryDTO(
        course.getExternalId(),
        course.getName(),
        calculateCoursePar(course),
        course.getHoles().size());
  }

  private static HoleDTO toHole(HoleEntity hole) {
    return new HoleDTO(
        hole.getHoleNumber(),
        hole.getHoleNumber(),
        hole.getDistance(),
        hole.getPar());
  }

  private static int calculateCoursePar(CourseEntity course) {
    return course.getHoles().stream().mapToInt(HoleEntity::getPar).sum();
  }

}
