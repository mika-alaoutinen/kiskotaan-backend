package mikaa.players.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import mikaa.players.events.Player;

@Component
class PlayerConsumer {

  @KafkaListener(topics = KafkaTopic.PLAYERS, groupId = "players-group")
  void consume(Player player) {
    System.out.println("consume " + player);
  }

}
