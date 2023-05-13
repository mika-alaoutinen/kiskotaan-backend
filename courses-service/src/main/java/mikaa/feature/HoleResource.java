package mikaa.feature;

import org.modelmapper.ModelMapper;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import mikaa.api.HolesApi;
import mikaa.model.HoleDetailsDTO;
import mikaa.model.NewHoleDTO;

@RequiredArgsConstructor
class HoleResource implements HolesApi {

  private static final ModelMapper MAPPER = new ModelMapper();
  private final HoleService service;

  @Override
  @Transactional
  public void deleteHole(Integer id) {
    service.delete(id);
  }

  @Override
  public HoleDetailsDTO getHole(Integer id) {
    return mapHole(service.findOne(id));
  }

  @Override
  @Transactional
  public HoleDetailsDTO updateHole(Integer id, @Valid @NotNull NewHoleDTO newHole) {
    var hole = service.update(id, MAPPER.map(newHole, HoleEntity.class));
    return mapHole(hole);
  }

  private static HoleDetailsDTO mapHole(HoleEntity hole) {
    return MAPPER.map(hole, HoleDetailsDTO.class);
  }

}
