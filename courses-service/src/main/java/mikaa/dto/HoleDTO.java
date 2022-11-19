package mikaa.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import mikaa.feature.HoleEntity;

@RegisterForReflection
public record HoleDTO(long id, int number, int par, int distance) {

  public HoleDTO(HoleEntity entity) {
    this(entity.id, entity.holeNumber, entity.par, entity.distance);
  }
}
