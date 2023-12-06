package mikaa.streams;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serde;

import mikaa.kiskotaan.domain.CourseEvent;
import mikaa.kiskotaan.domain.CoursePayload;

public interface TopologyDescription<K, V extends SpecificRecord, U extends SpecificRecord> {

  record Topic<K, V>(String name, Serde<K> keySerde, Serde<V> valueSerde) {
  }

  record Description<K, V, U>(Topic<K, V> input, Topic<K, U> output) {
  }

  Description<K, V, U> description();

  public interface CoursesTopology extends TopologyDescription<Long, CourseEvent, CoursePayload> {
  }

}
