package mikaa.queries.scorecard;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import mikaa.feature.scorecard.ScoreCardEntity;

public interface ScoreCardReader {

  Uni<ScoreCardEntity> findOne(long externalId);

  Multi<ScoreCardEntity> findAll();

}
