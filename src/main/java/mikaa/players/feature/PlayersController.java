package mikaa.players.feature;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mikaa.api.PlayersApi;
import mikaa.model.NewPlayerDTO;
import mikaa.model.PlayerDTO;

@RequiredArgsConstructor
@RestController
class PlayersController implements PlayersApi {

  private static final ModelMapper MAPPER = new ModelMapper();
  private final PlayersService service;

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
    var players = service.getAll().stream().map(p -> MAPPER.map(p, PlayerDTO.class)).toList();
    return ResponseEntity.ok(players);
  }

  @Override
  public ResponseEntity<PlayerDTO> updatePlayer(Integer id, @Valid NewPlayerDTO newPlayer) {
    throw new UnsupportedOperationException("TODO");
  }

}
