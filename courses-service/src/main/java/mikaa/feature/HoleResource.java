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
    throw new UnsupportedOperationException("Unimplemented method 'addHole'");
  }

  @Override
  @Transactional
  public void deleteHole(Integer id, Integer number) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteHole'");
  }

  @Override
  public HoleDTO getHole(Integer id, Integer number) {
    throw new UnsupportedOperationException("Unimplemented method 'getHole'");
  }

  @Override
  public List<HoleDTO> getHoles(Integer id) {
    throw new UnsupportedOperationException("Unimplemented method 'getHoles'");

  }

  @Override
  @Transactional
  public HoleDTO updateHole(Integer id, Integer number, @Valid @NotNull UpdatedHoleDTO updatedHoleDTO) {
    throw new UnsupportedOperationException("Unimplemented method 'updateHole'");
  }

}
