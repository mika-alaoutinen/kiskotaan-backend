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
  @Channel(OutgoingChannels.SCORECARD_STATE)
  Emitter<Record<Long, ScoreCardEvent>> emitter;

  @Override
  public void scoreCardAdded(ScoreCardEntity entity) {
    send(Action.ADD, entity);
  }

  @Override
  public void scoreCardDeleted(ScoreCardEntity entity) {
    send(Action.DELETE, entity);
  }

  @Override
  public void scoreCardUpdated(ScoreCardEntity entity) {
    send(Action.UPDATE, entity);
  }

  private void send(Action action, ScoreCardEntity entity) {
    var event = new ScoreCardEvent(action, toStatePayload(entity));
    var record = Record.of(entity.getId(), event);
    emitter.send(record).toCompletableFuture().join();
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
