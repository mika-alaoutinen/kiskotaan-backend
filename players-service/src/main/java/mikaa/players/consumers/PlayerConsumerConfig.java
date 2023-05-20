package mikaa.players.consumers;

import java.util.HashMap;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import mikaa.PlayerPayload;

@Configuration
class PlayerConsumerConfig {

  @Bean
  ConsumerFactory<String, PlayerPayload> playerConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(
        new HashMap<>(),
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
