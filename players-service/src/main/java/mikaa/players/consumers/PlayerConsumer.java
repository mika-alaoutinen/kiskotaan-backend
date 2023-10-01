package mikaa.players.consumers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mikaa.kiskotaan.domain.PlayerEvent;
import mikaa.kiskotaan.domain.PlayerPayload;
import mikaa.players.producers.PlayerTopics;

/**
 * Used to test that player events are produced and sent successfully to Kafka.
 * The CountDownLatch is used to delay the execution of the test long enough for
 * the asynchronous consumer to receive the event. See this article for more
 * details: https://www.baeldung.com/spring-boot-kafka-testing.
 */
@Component
@Slf4j
public class PlayerConsumer {

  private CountDownLatch latch = new CountDownLatch(1);
  private PlayerPayload playerAdded;
  private PlayerPayload playerDeleted;
  private PlayerPayload playerUpdated;

  @KafkaListener(containerFactory = "playerListenerFactory", topics = PlayerTopics.PLAYER_STATE)
  void playerEvent(PlayerEvent event) {
    var payload = event.getPayload();

    switch (event.getAction()) {
      case ADD:
        playerAdded(payload);
        break;
      case UPDATE:
        playerUpdated(payload);
        break;
      case DELETE:
        playerDeleted(payload);
        break;
      case UNKNOWN:
      default:
        log.warn("Unknown player action {}", event.getAction());
        break;
    }
  }

  public PlayerPayload getPlayerAddedAwait() throws InterruptedException {
    await();
    return playerAdded;
  }

  public PlayerPayload getPlayerDeletedAwait() throws InterruptedException {
    await();
    return playerDeleted;
  }

  public PlayerPayload getPlayerUpdatedAwait() throws InterruptedException {
    await();
    return playerUpdated;
  }

  private boolean await() throws InterruptedException {
    return latch.await(1, TimeUnit.SECONDS);
  }

  private void playerAdded(PlayerPayload payload) {
    log.info("Player added {}", payload);
    this.playerAdded = payload;
  }

  private void playerUpdated(PlayerPayload payload) {
    log.info("Player updated {}", payload);
    this.playerUpdated = payload;
  }

  private void playerDeleted(PlayerPayload payload) {
    log.info("Player deleted {}", payload);
    this.playerDeleted = payload;
  }

}
