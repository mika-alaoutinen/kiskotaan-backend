package mikaa.players.producers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.domain.PlayerEvent;
import mikaa.kiskotaan.domain.PlayerPayload;

@Component
@RequiredArgsConstructor
class KafkaProducer implements PlayerProducer {

  private final KafkaTemplate<Long, PlayerEvent> template;

  @Override
  public void playerAdded(PlayerPayload payload) {
    var event = new PlayerEvent(payload.getId(), Action.ADD, payload);
    template.send(PlayerTopics.PLAYER_ADDED, payload.getId(), event);
  }

  @Override
  public void playerDeleted(PlayerPayload payload) {
    var event = new PlayerEvent(payload.getId(), Action.DELETE, payload);
    template.send(PlayerTopics.PLAYER_DELETED, payload.getId(), event);
  }

  @Override
  public void playerUpdated(PlayerPayload payload) {
    var event = new PlayerEvent(payload.getId(), Action.UPDATE, payload);
    template.send(PlayerTopics.PLAYER_UPDATED, payload.getId(), event);
  }

}
