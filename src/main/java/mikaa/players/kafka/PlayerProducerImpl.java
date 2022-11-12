package mikaa.players.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mikaa.players.events.Player;

@Component
@RequiredArgsConstructor
class PlayerProducerImpl implements PlayerProducer {

  private final KafkaTemplate<String, Player> template;

  @Override
  public void send(Player player) {
    template.send(KafkaTopic.PLAYERS, player);
  }
}
