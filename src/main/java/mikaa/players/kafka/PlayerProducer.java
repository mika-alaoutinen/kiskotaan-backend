package mikaa.players.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mikaa.players.events.Player;

@Component
@RequiredArgsConstructor
class PlayerProducer {

  private final KafkaTemplate<String, Player> template;

  void send(Player player) {
    template.send(KafkaTopic.PLAYERS, player);
  }
}
