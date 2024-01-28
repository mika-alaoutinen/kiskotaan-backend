package mikaa.producers;

import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.modelmapper.ModelMapper;

import io.smallrye.reactive.messaging.kafka.Record;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.config.InternalChannels;
import mikaa.config.OutgoingChannels;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.kiskotaan.domain.ScoreCardByHoleEvent;
import mikaa.kiskotaan.domain.ScoreCardByHolePayload;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardByHoleProducer {

  private final ModelMapper mapper;

  @Incoming(InternalChannels.SCORECARD_STATE)
  @Outgoing(OutgoingChannels.SCORECARD_BY_HOLE_STATE)
  Record<Long, ScoreCardByHoleEvent> process(ScoreCardRecord record) {
    var entity = record.entity();
    var event = new ScoreCardByHoleEvent(record.action(), toPayload(entity));
    return Record.of(entity.getId(), event);
  }

  private ScoreCardByHolePayload toPayload(ScoreCardEntity entity) {
    var playerIds = entity.getPlayers()
        .stream()
        .map(PlayerEntity::getExternalId)
        .toList();

    return new ScoreCardByHolePayload(
        entity.getId(),
        entity.getCourse().getExternalId(),
        playerIds,
        Map.of(),
        Map.of());
  }

}
