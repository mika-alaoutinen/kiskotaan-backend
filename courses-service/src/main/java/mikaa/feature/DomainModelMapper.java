package mikaa.feature;

import mikaa.domain.Course;
import mikaa.domain.Hole;

interface DomainModelMapper {

  static Course course(CourseEntity entity) {
    var holes = entity.getHoles()
        .stream()
        .map(hole -> new Hole(hole.getId(), hole.getNumber(), hole.getPar(), hole.getDistance()))
        .toList();

    return new Course(entity.getId(), entity.getName(), holes);
  }

  static Hole hole(HoleEntity entity) {
    return new Hole(entity.getId(), entity.getNumber(), entity.getPar(), entity.getDistance());
  }

}
