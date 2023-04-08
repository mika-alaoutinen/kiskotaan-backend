package mikaa.feature;

import mikaa.dto.HoleDTO;
import mikaa.kafka.holes.HolePayload;

interface HoleMapper {

  static HoleDTO dto(HoleEntity entity) {
    return new HoleDTO(entity.getId(), entity.getHoleNumber(), entity.getPar(), entity.getDistance());
  }

  static HolePayload payload(HoleEntity entity) {
    return new HolePayload(
        entity.getId(),
        entity.getCourse().getId(),
        entity.getHoleNumber(),
        entity.getPar(),
        entity.getDistance());
  }

}
