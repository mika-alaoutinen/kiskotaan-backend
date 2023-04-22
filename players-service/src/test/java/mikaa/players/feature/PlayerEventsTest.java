package mikaa.players.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import mikaa.players.events.PlayerEvents.PlayerEvent;
import mikaa.players.kafka.KafkaTopic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class PlayerEventsTest {

  @Autowired
  private EmbeddedKafkaBroker broker;

  @Autowired
  private PlayersRepository repository;

  @Autowired
  private PlayersService service;

  private Consumer<String, PlayerEvent> consumer;

  @BeforeEach
  void setup() {
    consumer = configureConsumer();
  }

  @Test
  void sends_event_on_new_player_added() throws Exception {
    service.add(new PlayerEntity("Pekka", "Kana"));

    var record = KafkaTestUtils.getSingleRecord(consumer, KafkaTopic.PLAYERS);
    var payload = record.value().payload();

    assertEquals("Pekka", payload.firstName());
    assertEquals("Kana", payload.lastName());
  }

  private Consumer<String, PlayerEvent> configureConsumer() {
    var configs = KafkaTestUtils.consumerProps(KafkaTopic.PLAYERS, "true", broker);
    configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    var keyDeserializer = new StringDeserializer();

    var valueDeserializer = new JsonDeserializer<PlayerEvent>();
    valueDeserializer.addTrustedPackages("*");

    var factory = new DefaultKafkaConsumerFactory<String, PlayerEvent>(
        configs, keyDeserializer, valueDeserializer);

    var consumer = factory.createConsumer();
    consumer.subscribe(List.of(KafkaTopic.PLAYERS));

    return consumer;
  }

}
