package mikaa.util;

import org.apache.kafka.clients.producer.ProducerRecord;

import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.course.CourseEvent;
import mikaa.kiskotaan.player.PlayerEvent;
import mikaa.streams.TopologyDescription.CoursesTopology;
import mikaa.streams.TopologyDescription.PlayersTopology;

@ApplicationScoped
@RequiredArgsConstructor
public class KafkaCompanionWrapper {

  private static final long SLEEP_MILLIS = 5000;

  private final CoursesTopology coursesTopology;
  private final PlayersTopology playersTopology;

  private KafkaCompanion kafka;

  // lateinit due to KafkaCompanion magic
  public void init(KafkaCompanion kafka) {
    this.kafka = kafka;
  }

  public void sendCourse(CourseEvent event) throws InterruptedException {
    var record = courseRecord(event);
    var inputTopic = coursesTopology.description().input();
    kafka.produce(inputTopic.keySerde(), inputTopic.valueSerde()).fromRecords(record);
    Thread.sleep(SLEEP_MILLIS);
  }

  public void sendPlayer(PlayerEvent event) throws InterruptedException {
    var record = playerRecord(event);
    var inputTopic = playersTopology.description().input();
    kafka.produce(inputTopic.keySerde(), inputTopic.valueSerde()).fromRecords(record);
    Thread.sleep(SLEEP_MILLIS);
  }

  private ProducerRecord<Long, CourseEvent> courseRecord(CourseEvent event) {
    var inputTopic = coursesTopology.description().input().name();
    var id = event.getPayload().getId();
    return new ProducerRecord<>(inputTopic, id, event);
  }

  private ProducerRecord<Long, PlayerEvent> playerRecord(PlayerEvent event) {
    var inputTopic = playersTopology.description().input().name();
    var id = event.getPayload().getId();
    return new ProducerRecord<>(inputTopic, id, event);
  }

}
