package mikaa.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import mikaa.course.CourseEntity;

@RegisterForReflection
public record CourseSummaryDTO(long id, String name, int holes, int par) {

  public CourseSummaryDTO(CourseEntity entity) {
    this(
        entity.id,
        entity.name,
        courseHoleCount(entity),
        coursePar(entity));
  }

  private static int courseHoleCount(CourseEntity entity) {
    return entity.holes.size();
  }

  private static int coursePar(CourseEntity entity) {
    return entity.holes.stream().mapToInt(h -> h.par).sum();
  }
}
