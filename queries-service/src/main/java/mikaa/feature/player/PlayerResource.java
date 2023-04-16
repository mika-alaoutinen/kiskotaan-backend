package mikaa.feature.player;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.api.PlayersApi;
import mikaa.model.PlayerDTO;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerResource implements PlayersApi {

  @Override
  public PlayerDTO getPlayer(Integer id) {
    throw new UnsupportedOperationException("Unimplemented method 'getPlayer'");
  }

  @Override
  public List<PlayerDTO> getPlayers(String name) {
    throw new UnsupportedOperationException("Unimplemented method 'getPlayers'");
  }

}
