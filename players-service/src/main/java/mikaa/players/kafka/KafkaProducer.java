package mikaa.players.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mikaa.players.events.PlayerEvents.PlayerEvent;

@Component
@RequiredArgsConstructor
class KafkaProducer implements PlayerProducer {

  private final KafkaTemplate<String, PlayerEvent> template;

  @Override
  public void send(PlayerEvent event) {
    template.send(PlayerTopics.PLAYER_ADDED, event);
  }

}
