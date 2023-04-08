package mikaa.feature;

import java.util.List;

import mikaa.kafka.courses.CoursePayload;
import mikaa.kafka.holes.HoleDTO;

interface CourseMapper {

  static CoursePayload course(CourseEntity entity) {
    return new CoursePayload(entity.getId(), entity.getName(), holes(entity));
  }

  private static List<HoleDTO> holes(CourseEntity entity) {
    return entity.getHoles().stream().map(HoleMapper::dto).toList();
  }

}
