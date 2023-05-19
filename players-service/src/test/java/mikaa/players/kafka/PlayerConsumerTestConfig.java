package mikaa.players.kafka;

import java.time.Duration;
import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import mikaa.kiskotaan.domain.PlayerPayload;
import mikaa.players.producers.PlayerTopics;

@Configuration
class PlayerConsumerTestConfig {

  @Bean
  Consumer<String, PlayerPayload> playerAddedConsumer(EmbeddedKafkaBroker broker) {
    return buildConsumer(broker, PlayerTopics.PLAYER_ADDED);
  }

  @Bean
  Consumer<String, PlayerPayload> playerDeletedConsumer(EmbeddedKafkaBroker broker) {
    return buildConsumer(broker, PlayerTopics.PLAYER_DELETED);
  }

  @Bean
  Consumer<String, PlayerPayload> playerUpdatedConsumer(EmbeddedKafkaBroker broker) {
    return buildConsumer(broker, PlayerTopics.PLAYER_UPDATED);
  }

  private static Consumer<String, PlayerPayload> buildConsumer(EmbeddedKafkaBroker broker, String topic) {
    var configs = KafkaTestUtils.consumerProps(topic, "true", broker);
    configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    var keyDeserializer = new StringDeserializer();

    var valueDeserializer = new JsonDeserializer<PlayerPayload>();
    valueDeserializer.addTrustedPackages("*");

    var factory = new DefaultKafkaConsumerFactory<String, PlayerPayload>(
        configs, keyDeserializer, valueDeserializer);

    var consumer = factory.createConsumer();
    consumer.subscribe(List.of(topic));
    consumer.poll(Duration.ofMillis(0));

    return consumer;
  }

}
