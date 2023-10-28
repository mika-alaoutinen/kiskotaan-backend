package mikaa.feature.scorecard;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import mikaa.consumers.scorecard.ScoreCardWriter;
import mikaa.kiskotaan.domain.ScoreCardPayload;
import mikaa.queries.scorecard.ScoreCardReader;
import mikaa.uni.UniDecorator;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardService implements ScoreCardReader, ScoreCardWriter {

  private final ScoreCardRepository repository;

  @Override
  public Uni<ScoreCardEntity> add(ScoreCardPayload payload) {
    throw new UnsupportedOperationException("Unimplemented method 'add'");
  }

  @Override
  public Uni<ScoreCardEntity> update(ScoreCardPayload payload) {
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public Uni<Void> delete(ScoreCardPayload payload) {
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

  @Override
  public Uni<ScoreCardEntity> findOne(long externalId) {
    return UniDecorator
        .from(repository.findByExternalId(externalId))
        .orThrow(new NotFoundException("Could not find score card with ID " + externalId));
  }

  @Override
  public Multi<ScoreCardEntity> findAll() {
    return repository.streamAll();
  }

}
