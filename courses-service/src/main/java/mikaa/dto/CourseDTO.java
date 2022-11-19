package mikaa.dto;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;
import mikaa.feature.CourseEntity;

@RegisterForReflection
public record CourseDTO(Long id, String name, List<HoleDTO> holes) {

  public CourseDTO(CourseEntity entity) {
    this(entity.id, entity.name, holes(entity));
  }

  private static List<HoleDTO> holes(CourseEntity entity) {
    return entity.holes.stream().map(HoleDTO::new).toList();
  }
}
