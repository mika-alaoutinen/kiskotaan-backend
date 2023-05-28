package mikaa.consumers.scorecard;

import io.smallrye.mutiny.Uni;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.kiskotaan.domain.ScorePayload;

public interface ScoreWriter {

  Uni<ScoreCardEntity> add(ScorePayload payload);

  Uni<Void> delete(ScorePayload payload);

}
