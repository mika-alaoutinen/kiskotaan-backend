package mikaa.feature.scorecard;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.consumers.scorecard.ScoreWriter;
import mikaa.kiskotaan.domain.ScorePayload;
import mikaa.queries.scorecard.ScoreCardReader;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreService implements ScoreWriter {

  private final ScoreCardReader scoreCards;
  private final ScoreCardRepository repository;

  @Override
  public Uni<ScoreCardEntity> add(ScorePayload payload) {
    throw new UnsupportedOperationException("Unimplemented method 'add'");
  }

  @Override
  public Uni<Void> delete(ScorePayload payload) {
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

}
