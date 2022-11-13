package mikaa.players.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mikaa.players.events.PlayerEvents.PlayerEvent;

@Component
@Slf4j
class PlayerConsumer implements KafkaConsumer {

  @KafkaListener(topics = KafkaTopic.PLAYERS, groupId = "players-group")
  public void consume(PlayerEvent event) {
    log.info("consumed event " + event);
  }

}
