package mikaa.consumers.player;

import io.smallrye.mutiny.Uni;
import mikaa.PlayerPayload;
import mikaa.feature.player.PlayerEntity;

public interface PlayerWriter {

  Uni<PlayerEntity> add(PlayerPayload payload);

  Uni<PlayerEntity> update(PlayerPayload payload);

  Uni<Void> delete(PlayerPayload payload);

}
