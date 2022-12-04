package mikaa.feature;

import mikaa.dto.HoleDTO;
import mikaa.dto.NewHoleDTO;

interface HoleMapper {

  static HoleDTO dto(HoleEntity entity) {
    return new HoleDTO(
        entity.getId(),
        entity.getCourse().getId(),
        entity.getHoleNumber(),
        entity.getPar(),
        entity.getDistance());
  }

  static HoleEntity entity(NewHoleDTO newHole) {
    return HoleEntity.from(newHole.number(), newHole.par(), newHole.distance());
  }

}
