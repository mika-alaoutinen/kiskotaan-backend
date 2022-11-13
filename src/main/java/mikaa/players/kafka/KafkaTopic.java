package mikaa.players.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
class KafkaTopic {

  static final String PLAYERS = "players";

  @Bean
  NewTopic playersTopic() {
    return TopicBuilder.name(PLAYERS).build();
  }

}
