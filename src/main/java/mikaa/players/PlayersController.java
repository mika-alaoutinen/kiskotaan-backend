package mikaa.players;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mikaa.api.PlayersApi;
import mikaa.model.NewPlayerDTO;
import mikaa.model.PlayerDTO;

@RestController
class PlayersController implements PlayersApi {

  @Override
  public ResponseEntity<PlayerDTO> addPlayer(@Valid NewPlayerDTO newPlayer) {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public ResponseEntity<Void> deletePlayer(Integer id) {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public ResponseEntity<PlayerDTO> getPlayer(Integer id) {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public ResponseEntity<List<PlayerDTO>> getPlayers() {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public ResponseEntity<PlayerDTO> updatePlayer(Integer id, @Valid NewPlayerDTO newPlayer) {
    throw new UnsupportedOperationException("TODO");
  }

}
