package mikaa.players.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mikaa.PlayerPayload;
import mikaa.players.producers.PlayerTopics;

/**
 * Used to test that player events are produced and sent successfully to Kafka.
 */
@Component
@Getter
@Slf4j
public class PlayerConsumer {

  private PlayerPayload playerAdded;
  private PlayerPayload playerDeleted;
  private PlayerPayload playerUpdated;

  @KafkaListener(containerFactory = "playerListenerFactory", topics = PlayerTopics.PLAYER_ADDED)
  void playerAdded(PlayerPayload payload) {
    log.info("Player added {}", payload);
    this.playerAdded = payload;
  }

  @KafkaListener(containerFactory = "playerListenerFactory", topics = PlayerTopics.PLAYER_DELETED)
  void playerDeleted(PlayerPayload payload) {
    log.info("Player deleted {}", payload);
    this.playerDeleted = payload;
  }

  @KafkaListener(containerFactory = "playerListenerFactory", topics = PlayerTopics.PLAYER_UPDATED)
  void playerUpdated(PlayerPayload payload) {
    log.info("Player updated {}", payload);
    this.playerUpdated = payload;
  }

}
