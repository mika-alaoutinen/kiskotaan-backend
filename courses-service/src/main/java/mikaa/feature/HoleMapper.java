package mikaa.feature;

import mikaa.dto.HoleDTO;
import mikaa.dto.NewHoleDTO;

interface HoleMapper {

  static HoleDTO hole(HoleEntity entity) {
    return new HoleDTO(entity.getId(), entity.getPar(), entity.getPar(), entity.getDistance());
  }

  static HoleEntity entity(NewHoleDTO newHole) {
    return new HoleEntity(newHole.number(), newHole.par(), newHole.distance());
  }

}
