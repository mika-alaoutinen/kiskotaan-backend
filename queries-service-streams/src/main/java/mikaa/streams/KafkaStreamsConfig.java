package mikaa.streams;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "mikaa.kafka-streams")
public interface KafkaStreamsConfig {

  InputTopics inputTopics();

  StateStores stateStores();

  interface InputTopics {
    String courses();

    String holes();

    String players();

    String scorecards();
  }

  interface StateStores {
    String courses();
  }

}
