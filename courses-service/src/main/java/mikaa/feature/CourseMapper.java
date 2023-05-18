package mikaa.feature;

import java.util.List;

import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.kiskotaan.domain.Hole;

interface CourseMapper {

  static CoursePayload toPayload(CourseEntity entity) {
    return new CoursePayload(entity.getId(), entity.getName(), holes(entity));
  }

  private static List<Hole> holes(CourseEntity entity) {
    return entity.getHoles().stream().map(CourseMapper::hole).toList();
  }

  private static Hole hole(HoleEntity entity) {
    return new Hole(
        entity.getId(),
        entity.getHoleNumber(),
        entity.getPar(),
        entity.getDistance());
  }

}
