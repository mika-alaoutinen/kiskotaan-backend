package mikaa.players.feature;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import mikaa.api.PlayersApi;
import mikaa.model.NewPlayerDTO;
import mikaa.model.PlayerDTO;
import mikaa.players.domain.NewPlayer;
import mikaa.players.domain.Player;

@RestController
@RequiredArgsConstructor
class PlayersController implements PlayersApi {

  private final PlayersService service;

  @Override
  public ResponseEntity<PlayerDTO> addPlayer(@Valid NewPlayerDTO newPlayer) {
    var savedPlayer = service.add(fromNewPlayerDto(newPlayer));
    return new ResponseEntity<PlayerDTO>(toDto(savedPlayer), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<Void> deletePlayer(Integer id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<PlayerDTO> getPlayer(Integer id) {
    return service.findOne(id)
        .map(PlayersController::toDto)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> notFound(id));
  }

  @Override
  public ResponseEntity<List<PlayerDTO>> getPlayers(@Valid Optional<String> nameFilter) {
    var players = service.findAll(nameFilter.orElse(""))
        .stream()
        .map(PlayersController::toDto)
        .toList();

    return ResponseEntity.ok(players);
  }

  @Override
  public ResponseEntity<PlayerDTO> updatePlayer(Integer id, @Valid NewPlayerDTO editedPlayer) {
    var edited = fromNewPlayerDto(editedPlayer);

    return service.update(id, edited)
        .map(PlayersController::toDto)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> notFound(id));
  }

  private static NewPlayer fromNewPlayerDto(NewPlayerDTO dto) {
    return new NewPlayer(dto.getFirstName(), dto.getLastName());
  }

  private static PlayerDTO toDto(Player player) {
    return new PlayerDTO(BigDecimal.valueOf(player.id()), player.firstName(), player.lastName());
  }

  private static ResponseStatusException notFound(int id) {
    String msg = "Player with ID %s not found".formatted(id);
    return new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
  }
}
