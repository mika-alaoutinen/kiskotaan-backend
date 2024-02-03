package mikaa.producers;

import io.smallrye.reactive.messaging.kafka.Record;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.scorecards.RoundResult;
import mikaa.kiskotaan.scorecards.ScoreCardByHoleEvent;
import mikaa.kiskotaan.scorecards.ScoreCardByHolePayload;
import mikaa.kiskotaan.scorecards.ScoreCardByPlayerEvent;
import mikaa.kiskotaan.scorecards.ScoreCardByPlayerPayload;
import mikaa.kiskotaan.scorecards.ScoreEntry;
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
  Emitter<Record<Long, ScoreCardByPlayerEvent>> byPlayerEmitter;

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
    var event = new ScoreCardByPlayerEvent(action, toStatePayload(entity));
    var record = Record.of(entity.getId(), event);
    byPlayerEmitter.send(record).toCompletableFuture().join();
  }

  private ScoreCardByHolePayload toPayload(ScoreCardEntity entity) {
    var results = ScoreLogic.calculateScoresByHole(entity)
        .getResults()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapper.map(entry.getValue(), RoundResult.class)));

    return new ScoreCardByHolePayload(
        entity.getId(),
        entity.getCourse().getExternalId(),
        getPlayerIds(entity),
        results);
  }

  private ScoreCardByPlayerPayload toStatePayload(ScoreCardEntity entity) {
    var scoresByHole = ScoreLogic.calculateScoresByHole(entity);

    var results = scoresByHole.getResults()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapper.map(entry.getValue(), RoundResult.class)));

    var scores = ScoreLogic.calculateScoresByPlayer(entity)
        .getScores()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapMany(entry.getValue(), ScoreEntry.class)));

    return new ScoreCardByPlayerPayload(
        entity.getId(),
        entity.getCourse().getExternalId(),
        getPlayerIds(entity),
        results,
        scores);
  }

  private static List<Long> getPlayerIds(ScoreCardEntity scoreCard) {
    return scoreCard.getPlayers()
        .stream()
        .map(PlayerEntity::getExternalId)
        .toList();
  }

  private <T, R> List<R> mapMany(Collection<T> entities, Class<R> type) {
    return entities.stream().map(e -> mapper.map(e, type)).toList();
  }

}
