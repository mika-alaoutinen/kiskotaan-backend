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
    send(Action.ADD, payload);
  }

  @Override
  public void playerDeleted(PlayerPayload payload) {
    send(Action.DELETE, payload);
  }

  @Override
  public void playerUpdated(PlayerPayload payload) {
    send(Action.UPDATE, payload);
  }

  private void send(Action action, PlayerPayload payload) {
    var event = new PlayerEvent(action, payload);
    template.send(PlayerTopics.PLAYER_STATE, payload.getId(), event);
  }

}
