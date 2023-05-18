package mikaa.feature.player;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import mikaa.PlayerPayload;
import mikaa.consumers.player.PlayerWriter;
import mikaa.queries.player.PlayerReader;
import mikaa.uni.UniDecorator;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerService implements PlayerReader, PlayerWriter {

  private final PlayerRepository repository;

  @Override
  public Uni<PlayerEntity> add(PlayerPayload payload) {
    return repository.persist(toPlayer(payload));
  }

  public Uni<PlayerEntity> update(PlayerPayload payload) {
    return UniDecorator.from(repository.findByExternalId(payload.id()))
        .map(player -> {
          player.setFirstName(payload.firstName());
          player.setLastName(payload.lastName());
          return player;
        })
        .flatMap(repository::update)
        .unwrap();
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
