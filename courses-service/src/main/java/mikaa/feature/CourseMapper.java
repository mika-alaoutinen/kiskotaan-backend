package mikaa.feature;

import java.util.List;

import mikaa.kafka.courses.CoursePayload;
import mikaa.kafka.holes.HolePayload;

interface CourseMapper {

  static CoursePayload course(CourseEntity entity) {
    return new CoursePayload(entity.getId(), entity.getName(), holes(entity));
  }

  private static List<HolePayload> holes(CourseEntity entity) {
    return entity.getHoles().stream().map(HoleMapper::dto).toList();
  }

}
