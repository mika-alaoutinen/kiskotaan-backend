package mikaa.feature.player;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.kafka.player.PlayerDTO;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerService implements PlayerFinder {

  private final PlayerRepository repository;

  @Override
  public PlayerEntity findOrThrow(long id) {
    return repository.findByIdOptional(id).orElseThrow(() -> notFound(id));
  }

  void add(PlayerDTO newPlayer) {
    var entity = new PlayerEntity(null, newPlayer.firstName(), newPlayer.lastName());
    repository.persist(entity);
  }

  void delete(PlayerDTO newPlayer) {
  }

  void update(PlayerDTO newPlayer) {
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find player with id " + id);
  }

}
