package mikaa.consumers.scorecard;

import io.smallrye.mutiny.Uni;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.kiskotaan.domain.ScoreCardPayload;

public interface ScoreCardWriter {

  Uni<ScoreCardEntity> add(ScoreCardPayload payload);

  Uni<Void> delete(ScoreCardPayload payload);

}