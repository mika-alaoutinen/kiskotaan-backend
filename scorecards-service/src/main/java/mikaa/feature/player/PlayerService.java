package mikaa.feature.player;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.scorecards.PlayerPayload;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerService implements PlayerFinder {

  private final PlayerRepository repository;

  @Override
  public PlayerEntity findOrThrow(long id) {
    return repository.findByExternalId(id).orElseThrow(() -> notFound(id));
  }

  void add(PlayerPayload player) {
    var entity = new PlayerEntity(player.getId(), player.getFirstName(), player.getLastName());
    repository.persist(entity);
  }

  void delete(PlayerPayload player) {
    repository.findByExternalId(player.getId()).ifPresent(entity -> {
      entity.removeFromScoreCards();
      repository.delete(entity);
    });
  }

  void update(PlayerPayload player) {
    repository.findByExternalId(player.getId())
        .map(entity -> {
          entity.setFirstName(player.getFirstName());
          entity.setLastName(player.getLastName());
          return entity;
        })
        .ifPresent(repository::persist);
  }

  private static NotFoundException notFound(long id) {
    return new NotFoundException("Could not find player with id " + id);
  }

}
