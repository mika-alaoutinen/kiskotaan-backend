package mikaa.players.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import mikaa.players.events.PlayerEvents.PlayerEvent;

@Component
class PlayerConsumer {

  @KafkaListener(topics = KafkaTopic.PLAYERS, groupId = "players-group")
  void consume(PlayerEvent event) {
    System.out.println("consume " + event);
  }

}
