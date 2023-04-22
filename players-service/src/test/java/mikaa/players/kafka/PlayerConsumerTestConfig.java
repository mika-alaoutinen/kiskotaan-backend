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

import mikaa.players.events.PlayerEvents.PlayerEvent;

@Configuration
class PlayerConsumerTestConfig {

  /**
   * Configures a Kafka consumer bean that listens to the "Player.player" topic.
   * Used for testing Kafka event producer.
   * 
   * @param broker Embedded Kafka broker
   * @return consumer bean
   */
  @Bean
  Consumer<String, PlayerEvent> playerConsumer(EmbeddedKafkaBroker broker) {
    var configs = KafkaTestUtils.consumerProps(KafkaTopic.PLAYERS, "true", broker);
    configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    var keyDeserializer = new StringDeserializer();

    var valueDeserializer = new JsonDeserializer<PlayerEvent>();
    valueDeserializer.addTrustedPackages("*");

    var factory = new DefaultKafkaConsumerFactory<String, PlayerEvent>(
        configs, keyDeserializer, valueDeserializer);

    var consumer = factory.createConsumer();
    consumer.subscribe(List.of(KafkaTopic.PLAYERS));
    consumer.poll(Duration.ofMillis(0));

    return consumer;
  }

}
