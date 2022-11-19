package mikaa.dto;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;
import mikaa.feature.CourseEntity;

@RegisterForReflection
public record CourseDTO(long id, String name, List<HoleDTO> holes) {

  public CourseDTO(CourseEntity entity) {
    this(entity.getId(), entity.getName(), holes(entity));
  }

  private static List<HoleDTO> holes(CourseEntity entity) {
    return entity.getHoles().stream().map(HoleDTO::new).toList();
  }
}
