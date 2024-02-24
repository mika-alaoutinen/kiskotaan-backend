package mikaa.feature;

import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import mikaa.api.HolesApi;
import mikaa.domain.Hole;
import mikaa.domain.NewHole;
import mikaa.domain.UpdatedHole;
import mikaa.model.HoleDTO;
import mikaa.model.UpdatedHoleDTO;

@RequiredArgsConstructor
class HoleResource implements HolesApi {

  private final HoleService service;

  @Override
  @Transactional
  public HoleDTO addHole(Integer id, @Valid @NotNull HoleDTO holeDTO) {
    var newHole = new NewHole(holeDTO.getNumber(), holeDTO.getPar(), holeDTO.getDistance());
    var hole = service.add(id, newHole);
    return mapHole(hole);
  }

  @Override
  @Transactional
  public void deleteHole(Integer id, Integer number) {
    service.delete(id, number);
  }

  @Override
  public HoleDTO getHole(Integer id, Integer number) {
    return mapHole(service.findOne(id, number));
  }

  @Override
  public List<HoleDTO> getHoles(Integer id) {
    return service.findHoles(id)
        .stream()
        .map(HoleResource::mapHole)
        .toList();
  }

  @Override
  @Transactional
  public HoleDTO updateHole(Integer id, Integer number, @Valid @NotNull UpdatedHoleDTO updatedHole) {
    var hole = service.update(id, number, new UpdatedHole(updatedHole.getPar(), updatedHole.getDistance()));
    return mapHole(hole);
  }

  private static HoleDTO mapHole(Hole hole) {
    var dto = new HoleDTO();
    dto.setNumber(hole.number());
    dto.setPar(hole.par());
    dto.setDistance(hole.distance());
    return dto;
  }

}
