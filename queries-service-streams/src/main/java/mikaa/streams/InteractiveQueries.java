package mikaa.streams;

import java.util.Optional;
import java.util.stream.Stream;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.CoursePayload;

@ApplicationScoped
@RequiredArgsConstructor
public class InteractiveQueries {

  private final KafkaStreamsConfig config;
  private final KafkaStreams streams;

  public Stream<CoursePayload> allCourses() {
    var iterator = tryGetCourseStore().all();

    return Stream.generate(() -> null)
        .takeWhile(x -> iterator.hasNext())
        .map(x -> iterator.next())
        .map(keyValue -> keyValue.value);
  }

  public Optional<CoursePayload> findCourse(long id) {
    return Optional.ofNullable(tryGetCourseStore().get(id));
  }

  private ReadOnlyKeyValueStore<Long, CoursePayload> tryGetCourseStore() {
    var storeName = config.stateStores().courses();
    var time = System.currentTimeMillis();
    var end = time + 3000;

    // Wait for 3 seconds
    while (System.currentTimeMillis() < end) {
      try {
        return streams.store(StoreQueryParameters.fromNameAndType(storeName, QueryableStoreTypes.keyValueStore()));
      } catch (InvalidStateStoreException e) {
        // Ignore, store not ready yet
      }
    }

    var errorMsg = String.format("State store %s is unavailable", storeName);
    throw new InvalidStateStoreException(errorMsg);
  }

}
