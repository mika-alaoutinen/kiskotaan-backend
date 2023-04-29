package mikaa.players.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class PlayerTopics {

  public static final String PLAYER_ADDED = "Players.player_added";
  public static final String PLAYER_DELETED = "Players.player_deleted";
  public static final String PLAYER_UPDATED = "Players.player_updated";

  @Bean
  NewTopic playerAddedTopic() {
    return TopicBuilder.name(PLAYER_ADDED).build();
  }

  @Bean
  NewTopic playerDeletedTopic() {
    return TopicBuilder.name(PLAYER_DELETED).build();
  }

  @Bean
  NewTopic playerUpdatedTopic() {
    return TopicBuilder.name(PLAYER_UPDATED).build();
  }

}
