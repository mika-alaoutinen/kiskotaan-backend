package mikaa.players.feature;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import mikaa.players.events.PlayerPayload;
import mikaa.players.events.PlayerEvents.PlayerEvent;
import mikaa.players.kafka.PlayerTopics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.kafka.clients.consumer.Consumer;

// Reset Kafka state before each test. Terrible for performance but makes writing tests easier.
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@EmbeddedKafka(partitions = 1)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PlayerEventsTest {

  // See PlayerConsumer.java in kafka test package
  @Autowired
  private Consumer<String, PlayerEvent> consumer;

  @Autowired
  private PlayersRepository repository;

  @Autowired
  private PlayersService service;

  @Test
  void sends_event_on_new_player_added() {
    service.add(new PlayerEntity("Pekka", "Kana"));
    assertPlayer(getPayload(), "Pekka", "Kana");
  }

  @Test
  void sends_event_on_player_update() {
    var player = repository.save(new PlayerEntity("Pekka", "Kana"));
    service.update(player.getId(), new PlayerEntity("Kalle", "Kukko"));
    assertPlayer(getPayload(), "Kalle", "Kukko");
  }

  @Test
  void sends_event_on_player_delete() {
    var player = repository.save(new PlayerEntity("Delete", "Me"));
    service.delete(player.getId());
    assertPlayer(getPayload(), "Delete", "Me");
  }

  private static void assertPlayer(PlayerPayload player, String firstName, String lastName) {
    assertEquals(firstName, player.firstName());
    assertEquals(lastName, player.lastName());
  }

  private PlayerPayload getPayload() {
    return KafkaTestUtils.getSingleRecord(consumer, PlayerTopics.PLAYER_ADDED).value().payload();
  }

}
