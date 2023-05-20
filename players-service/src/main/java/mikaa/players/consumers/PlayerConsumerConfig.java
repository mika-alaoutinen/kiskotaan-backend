package mikaa.players.consumers;

import java.util.HashMap;
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

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  ConsumerFactory<String, PlayerPayload> playerConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "players");

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
