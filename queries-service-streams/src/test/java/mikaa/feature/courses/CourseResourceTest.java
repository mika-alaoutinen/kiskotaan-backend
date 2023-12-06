package mikaa.feature.courses;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;
import jakarta.inject.Inject;
import mikaa.graphql.QueryClient;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.domain.CourseEvent;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.kiskotaan.domain.Hole;
import mikaa.streams.TopologyDescription.CoursesTopology;

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
@TestInstance(Lifecycle.PER_CLASS)
class CourseResourceTest {

  @Inject
  private CoursesTopology topology;

  @InjectKafkaCompanion
  private KafkaCompanion kafka;

  @BeforeAll
  void sendKafkaEvents() throws InterruptedException {
    var inputTopic = topology.description().input();

    kafka.produce(inputTopic.keySerde(), inputTopic.valueSerde())
        .fromRecords(createRecord(1, "Laajis"), createRecord(2, "Kippis"));

    Thread.sleep(5000);
  }

  @Test
  void should_return_courses() {
    String query = """
        {
          courses {
            id
            name
            par
          }
        }
        """;

    QueryClient.query(query)
        .statusCode(200)
        .body("data.courses[0].name", Matchers.is("Laajis"));
  }

  @Test
  void should_return_course_by_id() {
    String query = """
        {
          course(id: 1) {
            id
            name
            par
          }
        }
        """;

    QueryClient.query(query)
        .statusCode(200)
        .body("data.course.name", Matchers.is("Laajis"));
  }

  private ProducerRecord<Long, CourseEvent> createRecord(long id, String name) {
    var inputTopic = topology.description().input();
    var payload = new CoursePayload(id, name, List.of(new Hole(111l, 1, 4, 120)));
    return new ProducerRecord<>(inputTopic.name(), id, new CourseEvent(Action.ADD, payload));
  }

}
