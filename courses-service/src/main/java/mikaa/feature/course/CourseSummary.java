package mikaa.feature.course;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import mikaa.feature.hole.HoleEntity;

/*
 * Use value class over record due to ModelMapper compatibility.
 * ModelMapper expects fields to have traditional getters.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Value
class CourseSummary {
  private final Long id;
  private final String name;
  private final int holes;
  private final int par;

  static CourseSummary from(CourseEntity entity) {
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

}
