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
import org.springframework.kafka.support.serializer.JsonDeserializer;

import mikaa.PlayerPayload;

@Configuration
class PlayerConsumerConfig {

  private final String bootstrapServers;

  PlayerConsumerConfig(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
    this.bootstrapServers = bootstrapServers;
  }

  @Bean
  ConsumerFactory<String, PlayerPayload> playerConsumerFactory() {
    Map<String, Object> props = Map.of(
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
        ConsumerConfig.GROUP_ID_CONFIG, "players");

    return new DefaultKafkaConsumerFactory<>(
        props,
        new StringDeserializer(),
        new JsonDeserializer<>(PlayerPayload.class));
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<String, PlayerPayload> playerListenerFactory(
      ConsumerFactory<String, PlayerPayload> consumerFactory) {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, PlayerPayload>();
    factory.setConsumerFactory(consumerFactory);
    return factory;
  }

}
