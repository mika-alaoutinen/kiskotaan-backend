package mikaa.players.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
class KafkaTopicsConfig {

  @Bean
  NewTopic playersTopic(@Value("${kafka.topic-name}") String topic) {
    return TopicBuilder.name(topic).build();
  }

}
