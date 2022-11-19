package mikaa.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import mikaa.feature.CourseEntity;

@RegisterForReflection
public record CourseSummaryDTO(long id, String name, int holes, int par) {

  public CourseSummaryDTO(CourseEntity entity) {
    this(
        entity.getId(),
        entity.getName(),
        courseHoleCount(entity),
        coursePar(entity));
  }

  private static int courseHoleCount(CourseEntity entity) {
    return entity.getHoles().size();
  }

  private static int coursePar(CourseEntity entity) {
    return entity.getHoles().stream().mapToInt(h -> h.par).sum();
  }
}
