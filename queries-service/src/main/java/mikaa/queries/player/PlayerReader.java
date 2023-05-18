package mikaa.queries.player;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import mikaa.feature.player.PlayerEntity;

public interface PlayerReader {

  Uni<PlayerEntity> findOne(long externalId);

  Multi<PlayerEntity> findAll();

}
