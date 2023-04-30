package mikaa.feature.player;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.events.player.PlayerPayload;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerService implements PlayerFinder {

  private final PlayerRepository repository;

  @Override
  public PlayerEntity findOrThrow(long id) {
    return repository.findByExternalId(id).orElseThrow(() -> notFound(id));
  }

  void add(PlayerPayload player) {
    var entity = new PlayerEntity(player.id(), player.firstName(), player.lastName());
    repository.persist(entity);
  }

  void delete(PlayerPayload player) {
    repository.findByExternalId(player.id()).ifPresent(entity -> {
      entity.removeFromScoreCards();
      repository.delete(entity);
    });
  }

  void update(PlayerPayload player) {
    repository.findByExternalId(player.id())
        .map(entity -> {
          entity.setFirstName(player.firstName());
          entity.setLastName(player.lastName());
          return entity;
        })
        .ifPresent(repository::persist);
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find player with id " + id);
  }

}
