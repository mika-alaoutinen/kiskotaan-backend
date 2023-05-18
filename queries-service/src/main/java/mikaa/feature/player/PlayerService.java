package mikaa.feature.player;

import java.util.function.Function;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import mikaa.PlayerPayload;
import mikaa.consumers.player.PlayerWriter;
import mikaa.queries.player.PlayerReader;
import mikaa.utils.UniFn;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerService implements PlayerReader, PlayerWriter {

  private final PlayerRepository repository;

  @Override
  public Uni<PlayerEntity> add(PlayerPayload payload) {
    return repository.persist(toPlayer(payload));
  }

  // Maybe add decorator for working with Unis?
  @Override
  public Uni<PlayerEntity> update(PlayerPayload payload) {
    Function<PlayerEntity, PlayerEntity> update = player -> {
      player.setFirstName(payload.firstName());
      player.setLastName(payload.lastName());
      return player;
    };

    Function<PlayerEntity, Uni<? extends PlayerEntity>> persist = player -> {
      return repository.update(player);
    };

    var maybePlayer = repository.findByExternalId(payload.id());
    var updated = UniFn.map(maybePlayer, update);
    return UniFn.flatMap(updated, persist);
  }

  @Override
  public Uni<Void> delete(PlayerPayload payload) {
    return repository.findByExternalId(payload.id())
        .onItem()
        .ifNotNull()
        .transformToUni(player -> repository.delete(player));
  }

  @Override
  public Uni<PlayerEntity> findOne(long externalId) {
    return repository.findByExternalId(externalId)
        .onItem()
        .ifNull()
        .failWith(new NotFoundException("Could not find player with ID " + externalId));
  }

  @Override
  public Multi<PlayerEntity> findAll() {
    return repository.streamAll();
  }

  private static PlayerEntity toPlayer(PlayerPayload payload) {
    return new PlayerEntity(payload.id(), payload.firstName(), payload.lastName());
  }

}
