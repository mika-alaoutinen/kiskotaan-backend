package mikaa.players.feature;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import mikaa.api.PlayersApi;
import mikaa.model.NewPlayerDTO;
import mikaa.model.PlayerDTO;

@RestController
@RequiredArgsConstructor
class PlayersController implements PlayersApi {

  private static final ModelMapper MAPPER = new ModelMapper();
  private final PlayersService service;

  @Override
  public ResponseEntity<PlayerDTO> addPlayer(@Valid NewPlayerDTO newPlayer) {
    var savedPlayer = service.add(MAPPER.map(newPlayer, PlayerEntity.class));
    var response = MAPPER.map(savedPlayer, PlayerDTO.class);
    return new ResponseEntity<PlayerDTO>(response, HttpStatus.CREATED);
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
  public ResponseEntity<List<PlayerDTO>> getPlayers(
      @Valid Optional<String> firstName,
      @Valid Optional<String> lastName) {
    var players = service.findAll()
        .stream()
        .map(PlayersController::toDto)
        .toList();

    return ResponseEntity.ok(players);
  }

  @Override
  public ResponseEntity<PlayerDTO> updatePlayer(Integer id, @Valid NewPlayerDTO editedPlayer) {
    var edited = MAPPER.map(editedPlayer, PlayerEntity.class);

    return service.update(id, edited)
        .map(PlayersController::toDto)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> notFound(id));
  }

  private static PlayerDTO toDto(PlayerEntity player) {
    return MAPPER.map(player, PlayerDTO.class);
  }

  private static ResponseStatusException notFound(int id) {
    String msg = "Player with ID %s not found".formatted(id);
    return new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
  }
}
