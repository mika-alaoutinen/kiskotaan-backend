package mikaa.players.producers;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class PlayerTopics {

  public static final String PLAYER_STATE = "Players-player_state";

  @Bean
  NewTopic playerStateTopic() {
    return TopicBuilder.name(PLAYER_STATE).build();
  }

}
