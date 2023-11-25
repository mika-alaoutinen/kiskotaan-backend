package mikaa.feature.courses;

import java.util.Optional;
import java.util.stream.Stream;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.streams.InteractiveQueries;
import mikaa.streams.KafkaStreamsConfig;

@ApplicationScoped
@RequiredArgsConstructor
class CourseStore {

  private final InteractiveQueries queries;
  private final KafkaStreamsConfig config;

  Stream<CoursePayload> streamAll() {
    return queries.streamAll(storeName());
  }

  Optional<CoursePayload> findById(long id) {
    return queries.findByKey(id, storeName());
  }

  private String storeName() {
    return config.stateStores().courses();
  }

}
