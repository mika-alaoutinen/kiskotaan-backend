package mikaa.streams;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serde;

import mikaa.kiskotaan.course.CourseEvent;
import mikaa.kiskotaan.course.CoursePayload;
import mikaa.kiskotaan.player.PlayerEvent;
import mikaa.kiskotaan.player.PlayerPayload;

public interface TopologyDescription<K, V extends SpecificRecord, U extends SpecificRecord> {

  record Topic<K, V>(String name, Serde<K> keySerde, Serde<V> valueSerde) {
  }

  record Description<K, V, U>(Topic<K, V> input, Topic<K, U> output) {
  }

  Description<K, V, U> description();

  public interface CoursesTopology extends TopologyDescription<Long, CourseEvent, CoursePayload> {
  }

  public interface PlayersTopology extends TopologyDescription<Long, PlayerEvent, PlayerPayload> {
  }

}
