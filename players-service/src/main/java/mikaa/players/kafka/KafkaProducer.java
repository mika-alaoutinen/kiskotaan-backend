package mikaa.players.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class KafkaProducer implements PlayerProducer {

  private final KafkaTemplate<String, PlayerPayload> template;

  @Override
  public void playerAdded(PlayerPayload payload) {
    template.send(PlayerTopics.PLAYER_ADDED, payload);
  }

  @Override
  public void playerDeleted(PlayerPayload payload) {
    template.send(PlayerTopics.PLAYER_DELETED, payload);
  }

  @Override
  public void playerUpdated(PlayerPayload payload) {
    template.send(PlayerTopics.PLAYER_UPDATED, payload);
  }

}
