package mikaa.producers.score;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.ScorePayload;
import mikaa.config.OutgoingChannels;
import mikaa.feature.score.ScoreEntity;

@ApplicationScoped
class KafkaProducer implements ScoreProducer {

  @Inject
  @Channel(OutgoingChannels.Score.SCORE_ADDED)
  Emitter<ScorePayload> addEmitter;

  @Inject
  @Channel(OutgoingChannels.Score.SCORE_DELETED)
  Emitter<ScorePayload> deleteEmitter;

  @Override
  public void scoreAdded(ScoreEntity entity) {
    var payload = toPayload(entity);
    addEmitter.send(payload).toCompletableFuture().join();
  }

  @Override
  public void scoreDeleted(ScoreEntity entity) {
    var payload = toPayload(entity);
    deleteEmitter.send(payload).toCompletableFuture().join();
  }

  private static ScorePayload toPayload(ScoreEntity entity) {
    return new ScorePayload(
        entity.getId(),
        entity.getPlayer().getExternalId(),
        entity.getScorecard().getId(),
        entity.getHole(),
        entity.getScore());
  }

}
