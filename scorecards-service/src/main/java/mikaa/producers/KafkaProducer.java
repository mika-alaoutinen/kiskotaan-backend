package mikaa.producers;

import java.util.stream.Collectors;

import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.domain.PlayerScore;
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
  @Channel(OutgoingChannels.SCORECARD_STATE)
  Emitter<Record<Long, ScoreCardStatePayload>> emitter;

  @Override
  public void scoreCardAdded(ScoreCardEntity entity) {
    var payload = toStatePayload(entity);
    var record = Record.of(payload.getId(), payload);
    emitter.send(record).toCompletableFuture().join();
  }

  @Override
  public void scoreCardDeleted(ScoreCardEntity entity) {
    var payload = toStatePayload(entity);
    var record = Record.of(payload.getId(), payload);
    emitter.send(record).toCompletableFuture().join();
  }

  @Override
  public void scoreCardUpdated(ScoreCardEntity entity) {
    var payload = toStatePayload(entity);
    var record = Record.of(payload.getId(), payload);
    emitter.send(record).toCompletableFuture().join();
  }

  private void send(Action action, ScoreCardEntity entity) {
    var payload = toStatePayload(entity);
    var record = Record.of(payload.getId(), payload);
    emitter.send(record).toCompletableFuture().join();
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

}
