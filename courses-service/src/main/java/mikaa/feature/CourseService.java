package mikaa.feature;

import java.util.List;

import javax.inject.Singleton;

@Singleton
class CourseService {

  List<CourseSummary> findAll() {
    List<CourseEntity> entities = CourseEntity.listAll();
    return entities.stream().map(CourseService::toSummary).toList();
  }

  private static CourseSummary toSummary(CourseEntity entity) {
    var coursePar = entity.holes.stream().mapToInt(h -> h.par).sum();
    return new CourseSummary(entity.id, entity.name, entity.holes.size(), coursePar);
  }
}
