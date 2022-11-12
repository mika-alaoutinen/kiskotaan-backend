package mikaa.players.feature;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<PlayerDTO> getPlayer(Integer id) {
    var player = service.findOne(id)
        .map(p -> MAPPER.map(p, PlayerDTO.class))
        .orElseThrow(() -> PlayersController.notFound(id));

    return ResponseEntity.ok(player);
  }

  @Override
  public ResponseEntity<List<PlayerDTO>> getPlayers() {
    var players = service.findAll().stream().map(p -> MAPPER.map(p, PlayerDTO.class)).toList();
    return ResponseEntity.ok(players);
  }

  @Override
  public ResponseEntity<PlayerDTO> updatePlayer(Integer id, @Valid NewPlayerDTO newPlayer) {
    throw new UnsupportedOperationException("TODO");
  }

  private static ResponseStatusException notFound(int id) {
    String msg = "Player with ID %s not found".formatted(id);
    return new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
  }
}
