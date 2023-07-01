package mikaa.feature;

import java.util.List;

import org.modelmapper.ModelMapper;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import mikaa.api.HolesApi;
import mikaa.model.HoleDTO;
import mikaa.model.UpdatedHoleDTO;

@RequiredArgsConstructor
class HoleResource implements HolesApi {

  private static final ModelMapper MAPPER = new ModelMapper();
  private final HoleService service;

  @Override
  @Transactional
  public HoleDTO addHole(Integer id, @Valid @NotNull HoleDTO holeDTO) {
    var hole = service.add(id, MAPPER.map(holeDTO, HoleEntity.class));
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
    return service.findCourseHoles(id)
        .stream()
        .map(HoleResource::mapHole)
        .toList();
  }

  @Override
  @Transactional
  public HoleDTO updateHole(Integer id, Integer number, @Valid @NotNull UpdatedHoleDTO updatedHole) {
    var hole = service.update(id, number, MAPPER.map(updatedHole, HoleEntity.class));
    return mapHole(hole);
  }

  private static HoleDTO mapHole(HoleEntity entity) {
    return MAPPER.map(entity, HoleDTO.class);
  }

}
