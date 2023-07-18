package mikaa.producers;

import java.util.stream.Collectors;

import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.PlayerScore;
import mikaa.kiskotaan.domain.ScoreCardPayload;
import mikaa.kiskotaan.domain.ScoreCardStatePayload;
import mikaa.logic.ScoreLogic;
import mikaa.config.OutgoingChannels;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.scorecard.ScoreCardEntity;

@ApplicationScoped
class KafkaProducer implements ScoreCardProducer {

  @Inject
  private ModelMapper mapper;

  @Inject
  @Channel(OutgoingChannels.SCORECARD_ADDED)
  Emitter<Record<Long, ScoreCardPayload>> addEmitter;

  @Inject
  @Channel(OutgoingChannels.SCORECARD_DELETED)
  Emitter<Record<Long, ScoreCardPayload>> deleteEmitter;

  @Inject
  @Channel(OutgoingChannels.SCORECARD_UPDATED)
  Emitter<Record<Long, ScoreCardStatePayload>> stateEmitter;

  @Override
  public void scoreCardAdded(ScoreCardEntity entity) {
    var record = toRecord(toPayload(entity));
    addEmitter.send(record).toCompletableFuture().join();
  }

  @Override
  public void scoreCardDeleted(ScoreCardEntity entity) {
    var record = toRecord(toPayload(entity));
    deleteEmitter.send(record).toCompletableFuture().join();
  }

  @Override
  public void scoreCardUpdated(ScoreCardEntity entity) {
    var payload = toStatePayload(entity);
    var record = Record.of(payload.getId(), payload);
    stateEmitter.send(record).toCompletableFuture().join();
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

  private ScoreCardStatePayload toStatePayload(ScoreCardEntity entity) {
    var playerIds = entity.getPlayers()
        .stream()
        .map(PlayerEntity::getExternalId)
        .toList();

    var scores = ScoreLogic.calculatePlayerScores(entity)
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapper.map(entry.getValue(), PlayerScore.class)));

    return new ScoreCardStatePayload(
        entity.getId(),
        entity.getCourse().getExternalId(),
        playerIds,
        scores);
  }

  private static Record<Long, ScoreCardPayload> toRecord(ScoreCardPayload payload) {
    return Record.of(payload.getId(), payload);
  }

}
