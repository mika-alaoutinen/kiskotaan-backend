package mikaa.producers;

import java.util.stream.Collectors;

import io.smallrye.reactive.messaging.kafka.Record;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.config.InternalChannels;
import mikaa.config.OutgoingChannels;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.kiskotaan.domain.PlayerScore;
import mikaa.kiskotaan.domain.ScoreCardEvent;
import mikaa.kiskotaan.domain.ScoreCardPayload;
import mikaa.logic.ScoreLogic;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardByPlayerProducer {

  private final ModelMapper mapper;

  @Incoming(InternalChannels.SCORECARD_STATE)
  @Outgoing(OutgoingChannels.SCORECARD_BY_PLAYER_STATE)
  Record<Long, ScoreCardEvent> process(ScoreCardRecord record) {
    var entity = record.entity();
    var event = new ScoreCardEvent(record.action(), toPayload(entity));
    return Record.of(entity.getId(), event);
  }

  private ScoreCardPayload toPayload(ScoreCardEntity entity) {
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
