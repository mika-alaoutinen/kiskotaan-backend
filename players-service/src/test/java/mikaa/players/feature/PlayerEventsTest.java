package mikaa.players.feature;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import mikaa.players.events.PlayerEvents.PlayerEvent;
import mikaa.players.kafka.KafkaTopic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.kafka.clients.consumer.Consumer;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class PlayerEventsTest {

  @Autowired
  private PlayersRepository repository;

  @Autowired
  private PlayersService service;

  // See PlayerConsumer.java in kafka test package
  @Autowired
  private Consumer<String, PlayerEvent> consumer;

  @Test
  void sends_event_on_new_player_added() throws Exception {
    service.add(new PlayerEntity("Pekka", "Kana"));

    var record = KafkaTestUtils.getSingleRecord(consumer, KafkaTopic.PLAYERS);
    var payload = record.value().payload();

    assertEquals("Pekka", payload.firstName());
    assertEquals("Kana", payload.lastName());
  }

}
