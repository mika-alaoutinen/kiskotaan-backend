package mikaa.feature.player;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.PlayerPayload;
import mikaa.consumers.player.PlayerWriter;
import mikaa.queries.player.PlayerReader;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerService implements PlayerReader, PlayerWriter {

  private final PlayerRepository repository;

  @Override
  public Uni<PlayerEntity> add(PlayerPayload payload) {
    throw new UnsupportedOperationException("Unimplemented method 'add'");
  }

  @Override
  public Uni<PlayerEntity> update(PlayerPayload payload) {
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public Uni<Void> delete(PlayerPayload payload) {
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

  @Override
  public Uni<PlayerEntity> findOne(long externalId) {
    throw new UnsupportedOperationException("Unimplemented method 'findOne'");
  }

  @Override
  public Multi<PlayerEntity> findAll() {
    throw new UnsupportedOperationException("Unimplemented method 'findAll'");
  }

}
