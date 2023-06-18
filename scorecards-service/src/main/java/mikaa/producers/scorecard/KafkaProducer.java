package mikaa.producers.scorecard;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.ScoreCardPayload;
import mikaa.kiskotaan.domain.ScoreCardStatePayload;
import mikaa.config.OutgoingChannels;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.scorecard.ScoreCardEntity;

@ApplicationScoped
class KafkaProducer implements ScoreCardProducer {

  @Inject
  @Broadcast
  @Channel(OutgoingChannels.ScoreCard.SCORECARD_ADDED)
  Emitter<ScoreCardPayload> addEmitter;

  @Inject
  @Channel(OutgoingChannels.ScoreCard.SCORECARD_DELETED)
  Emitter<ScoreCardPayload> deleteEmitter;

  @Inject
  @Channel(OutgoingChannels.ScoreCard.SCORECARD_STATE)
  Emitter<ScoreCardStatePayload> stateEmitter;

  @Override
  public void scoreCardAdded(ScoreCardEntity entity) {
    var payload = toPayload(entity);
    addEmitter.send(payload).toCompletableFuture().join();
  }

  @Override
  public void scoreCardDeleted(ScoreCardEntity entity) {
    var payload = toPayload(entity);
    deleteEmitter.send(payload).toCompletableFuture().join();
  }

  @Override
  public void scoreCardUpdated(ScoreCardEntity entity) {
    var payload = toStatePayload(entity);
    stateEmitter.send(payload).toCompletableFuture().join();
  }

  private static ScoreCardPayload toPayload(ScoreCardEntity entity) {
    var playerIds = entity.getPlayers()
        .stream()
        .map(PlayerEntity::getExternalId)
        .toList();

    return new ScoreCardPayload(
        entity.getId(),
        entity.getCourse().getExternalId(),
        playerIds);
  }

  private static ScoreCardStatePayload toStatePayload(ScoreCardEntity entity) {
    return new ScoreCardStatePayload();
  }

}
