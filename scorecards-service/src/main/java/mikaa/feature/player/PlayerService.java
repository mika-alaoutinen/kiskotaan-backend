package mikaa.feature.player;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.events.player.PlayerPayload;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerService implements PlayerFinder {

  private final PlayerRepository repository;

  @Override
  public PlayerEntity findOrThrow(long id) {
    return repository.findByIdOptional(id).orElseThrow(() -> notFound(id));
  }

  void add(PlayerPayload player) {
    var entity = new PlayerEntity(null, player.firstName(), player.lastName());
    repository.persist(entity);
  }

  void delete(PlayerPayload player) {
    repository.deleteById(player.id());
  }

  void update(PlayerPayload player) {
    repository.findByIdOptional(player.id())
        .map(entity -> updateName(entity, player))
        .ifPresent(repository::persist);
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find player with id " + id);
  }

  private static PlayerEntity updateName(PlayerEntity entity, PlayerPayload updated) {
    entity.setFirstName(updated.firstName());
    entity.setLastName(updated.lastName());
    return entity;
  }

}
