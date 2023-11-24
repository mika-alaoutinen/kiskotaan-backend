package mikaa.streams;

import java.util.Optional;
import java.util.stream.Stream;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class InteractiveQueries {

  private final KafkaStreams streams;

  public <T extends SpecificRecord> Optional<T> findById(long id, String storeName) {
    ReadOnlyKeyValueStore<Long, T> store = tryGetStateStore(storeName);
    return Optional.ofNullable(store.get(id));
  }

  public <T extends SpecificRecord> Stream<T> streamAll(String storeName) {
    ReadOnlyKeyValueStore<Long, T> store = tryGetStateStore(storeName);
    var iterator = store.all();

    return Stream.generate(() -> null)
        .takeWhile(x -> iterator.hasNext())
        .map(x -> iterator.next())
        .map(keyValue -> keyValue.value);
  }

  private <T extends SpecificRecord> ReadOnlyKeyValueStore<Long, T> tryGetStateStore(String storeName) {
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
