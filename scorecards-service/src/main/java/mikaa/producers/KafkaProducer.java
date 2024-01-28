package mikaa.producers;

import io.smallrye.reactive.messaging.kafka.Record;

import java.util.stream.Collectors;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.domain.PlayerScore;
import mikaa.kiskotaan.domain.Result;
import mikaa.kiskotaan.domain.Score;
import mikaa.kiskotaan.domain.ScoreCardByHoleEvent;
import mikaa.kiskotaan.domain.ScoreCardByHolePayload;
import mikaa.kiskotaan.domain.ScoreCardEvent;
import mikaa.kiskotaan.domain.ScoreCardPayload;
import mikaa.logic.ScoreLogic;
import mikaa.config.OutgoingChannels;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.scorecard.ScoreCardEntity;

@ApplicationScoped
class KafkaProducer implements ScoreCardProducer {

  @Inject
  private ModelMapper mapper;

  @Inject
  @Channel(OutgoingChannels.SCORECARD_BY_HOLE_STATE)
  Emitter<Record<Long, ScoreCardByHoleEvent>> byHoleEmitter;

  @Inject
  @Channel(OutgoingChannels.SCORECARD_BY_PLAYER_STATE)
  Emitter<Record<Long, ScoreCardEvent>> byPlayerEmitter;

  @Override
  public void scoreCardAdded(ScoreCardEntity entity) {
    sendByPlayerEvent(Action.ADD, entity);
    sendByHoleEvent(Action.ADD, entity);
  }

  @Override
  public void scoreCardDeleted(ScoreCardEntity entity) {
    sendByPlayerEvent(Action.DELETE, entity);
    sendByHoleEvent(Action.DELETE, entity);
  }

  @Override
  public void scoreCardUpdated(ScoreCardEntity entity) {
    sendByPlayerEvent(Action.UPDATE, entity);
    sendByHoleEvent(Action.UPDATE, entity);
  }

  private void sendByHoleEvent(Action action, ScoreCardEntity entity) {
    var event = new ScoreCardByHoleEvent(action, toPayload(entity));
    var record = Record.of(entity.getId(), event);
    byHoleEmitter.send(record).toCompletableFuture().join();
  }

  private void sendByPlayerEvent(Action action, ScoreCardEntity entity) {
    var event = new ScoreCardEvent(action, toStatePayload(entity));
    var record = Record.of(entity.getId(), event);
    byPlayerEmitter.send(record).toCompletableFuture().join();
  }

  private ScoreCardByHolePayload toPayload(ScoreCardEntity entity) {
    var playerIds = entity.getPlayers()
        .stream()
        .map(PlayerEntity::getExternalId)
        .toList();

    var results = ScoreLogic.calculatePlayerScores(entity)
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapper.map(entry.getValue(), Result.class)));

    var scoresByHole = ScoreLogic.calculateScoresByHole(entity)
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapper.map(entry.getValue(), Score.class)));

    return new ScoreCardByHolePayload(
        entity.getId(),
        entity.getCourse().getExternalId(),
        playerIds,
        results,
        scoresByHole);
  }

  private ScoreCardPayload toStatePayload(ScoreCardEntity entity) {
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

    return new ScoreCardPayload(
        entity.getId(),
        entity.getCourse().getExternalId(),
        playerIds,
        scores);
  }

}
