package mikaa.feature;

import java.util.List;

import mikaa.events.courses.CoursePayload;
import mikaa.events.courses.HolePayload;

interface CourseMapper {

  static CoursePayload toPayload(CourseEntity entity) {
    return new CoursePayload(entity.getId(), entity.getName(), holes(entity));
  }

  private static List<HolePayload> holes(CourseEntity entity) {
    return entity.getHoles().stream().map(CourseMapper::hole).toList();
  }

  private static HolePayload hole(HoleEntity entity) {
    return new HolePayload(
        entity.getId(),
        entity.getHoleNumber(),
        entity.getPar(),
        entity.getDistance());
  }

}
