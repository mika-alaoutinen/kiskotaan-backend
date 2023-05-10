package mikaa.queries.course;

import mikaa.feature.course.Course;
import mikaa.feature.course.Hole;
import mikaa.queries.dto.CourseDTO;
import mikaa.queries.dto.CourseSummaryDTO;
import mikaa.queries.dto.HoleDTO;

interface CourseMapper {

  static CourseDTO toCourse(Course course) {
    var holes = course.getHoles().stream().map(CourseMapper::toHole).toList();

    return new CourseDTO(
        course.getExternalId(),
        course.getName(),
        calculateCoursePar(course),
        holes);
  }

  static CourseSummaryDTO toSummary(Course course) {
    return new CourseSummaryDTO(
        course.getExternalId(),
        course.getName(),
        calculateCoursePar(course),
        course.getHoles().size());
  }

  private static HoleDTO toHole(Hole hole) {
    return new HoleDTO(
        hole.getHoleNumber(),
        hole.getHoleNumber(),
        hole.getDistance(),
        hole.getPar());
  }

  private static int calculateCoursePar(Course course) {
    return course.getHoles().stream().mapToInt(Hole::getPar).sum();
  }

}
