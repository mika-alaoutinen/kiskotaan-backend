package mikaa.feature.player;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.kafka.player.PlayerDTO;
import mikaa.kafka.player.PlayerUpdater;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerService implements PlayerFinder, PlayerUpdater {

  private final PlayerRepository repository;

  @Override
  public PlayerEntity findOrThrow(long id) {
    return repository.findByIdOptional(id).orElseThrow(() -> notFound(id));
  }

  @Override
  public void add(PlayerDTO newPlayer) {
    var entity = new PlayerEntity(null, newPlayer.firstName(), newPlayer.lastName());
    repository.persist(entity);
  }

  @Override
  public void delete(PlayerDTO newPlayer) {
  }

  @Override
  public void update(PlayerDTO newPlayer) {
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find player with id " + id);
  }

}
