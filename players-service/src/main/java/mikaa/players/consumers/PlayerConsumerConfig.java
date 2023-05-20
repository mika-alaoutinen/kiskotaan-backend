package mikaa.players.consumers;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import io.apicurio.registry.serde.avro.AvroKafkaDeserializer;
import mikaa.kiskotaan.domain.PlayerPayload;

@Configuration
class PlayerConsumerConfig {

  private final String apicurioUrl;
  private final String bootstrapServers;

  PlayerConsumerConfig(
      @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
    this.apicurioUrl = "http://localhost:8000";
    this.bootstrapServers = bootstrapServers;
  }

  @Bean
  ConsumerFactory<String, PlayerPayload> playerConsumerFactory() {
    Map<String, Object> props = Map.of(
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
        ConsumerConfig.GROUP_ID_CONFIG, "players",
        "apicurio.registry.url", apicurioUrl,
        "apicurio.registry.use-specific-avro-reader", true);

    return new DefaultKafkaConsumerFactory<>(
        props,
        new StringDeserializer(),
        new AvroKafkaDeserializer<>());
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<String, PlayerPayload> playerListenerFactory(
      ConsumerFactory<String, PlayerPayload> consumerFactory) {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, PlayerPayload>();
    factory.setConsumerFactory(consumerFactory);
    return factory;
  }

}
