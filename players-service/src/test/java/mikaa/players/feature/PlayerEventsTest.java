package mikaa.players.feature;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import mikaa.PlayerPayload;
import mikaa.players.consumers.PlayerConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
class PlayerEventsTest {

  @Container
  static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka")).withKraft();

  @Autowired
  private PlayerConsumer consumer;

  @Autowired
  private PlayersRepository repository;

  @Autowired
  private PlayersService service;

  @DynamicPropertySource
  static void kafkaProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  @Test
  void sends_event_on_new_player_added() throws InterruptedException {
    service.add(new PlayerEntity("Pekka", "Kana"));
    var payload = consumer.getPlayerAddedAwait();
    assertPlayer(payload, "Pekka", "Kana");
  }

  @Test
  void sends_event_on_player_update() throws InterruptedException {
    var player = repository.save(new PlayerEntity("Pekka", "Kana"));
    service.update(player.getId(), new PlayerEntity("Kalle", "Kukko"));
    var payload = consumer.getPlayerUpdatedAwait();
    assertPlayer(payload, "Kalle", "Kukko");
  }

  @Test
  void sends_event_on_player_delete() throws InterruptedException {
    var player = repository.save(new PlayerEntity("Delete", "Me"));
    service.delete(player.getId());
    var payload = consumer.getPlayerDeletedAwait();
    assertPlayer(payload, "Delete", "Me");
  }

  private static void assertPlayer(PlayerPayload player, String firstName, String lastName) {
    assertEquals(firstName, player.firstName());
    assertEquals(lastName, player.lastName());
  }

}
