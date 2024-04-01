package mikaa.feature.players;

import java.util.Optional;
import java.util.stream.Stream;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.PlayerPayload;
import mikaa.streams.InteractiveQueries;
import mikaa.streams.KafkaStreamsConfig;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerStore {

  private final InteractiveQueries queries;
  private final KafkaStreamsConfig config;

  Stream<PlayerPayload> streamAll() {
    return queries.streamAll(storeName());
  }

  Optional<PlayerPayload> findById(long id) {
    return queries.findByKey(id, storeName());
  }

  private String storeName() {
    return config.stateStores().players();
  }

}
