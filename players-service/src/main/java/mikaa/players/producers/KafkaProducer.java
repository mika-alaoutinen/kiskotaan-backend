package mikaa.players.producers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.PlayerPayload;

@Component
@RequiredArgsConstructor
class KafkaProducer implements PlayerProducer {

  private final KafkaTemplate<Long, PlayerPayload> template;

  @Override
  public void playerAdded(PlayerPayload payload) {
    template.send(PlayerTopics.PLAYER_ADDED, payload.getId(), payload);
  }

  @Override
  public void playerDeleted(PlayerPayload payload) {
    template.send(PlayerTopics.PLAYER_DELETED, payload.getId(), payload);
  }

  @Override
  public void playerUpdated(PlayerPayload payload) {
    template.send(PlayerTopics.PLAYER_UPDATED, payload.getId(), payload);
  }

}
