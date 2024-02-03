package mikaa.consumers.scorecard;

import io.smallrye.mutiny.Uni;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.kiskotaan.scorecards.ScoreCardPayload;

public interface ScoreCardWriter {

  Uni<ScoreCardEntity> add(ScoreCardPayload payload);

  Uni<ScoreCardEntity> update(ScoreCardPayload payload);

  Uni<Void> delete(ScoreCardPayload payload);

}
