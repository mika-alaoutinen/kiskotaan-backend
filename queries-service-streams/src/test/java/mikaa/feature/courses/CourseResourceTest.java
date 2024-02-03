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
import mikaa.kiskotaan.courses.CourseEvent;
import mikaa.kiskotaan.courses.CoursePayload;
import mikaa.kiskotaan.domain.Hole;
import mikaa.streams.TopologyDescription.CoursesTopology;

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
@TestInstance(Lifecycle.PER_CLASS)
class CourseResourceTest {

  private static final String ALL_COURSES_QUERY = """
      {
        courses {
          id
          name
          par
        }
      }
      """;

  @Inject
  private CoursesTopology topology;

  @InjectKafkaCompanion
  private KafkaCompanion kafka;

  @BeforeAll
  void sendKafkaEvents() throws InterruptedException {
    sendRecords(createRecord(Action.ADD, 1, "Laajis"), createRecord(Action.ADD, 2, "Kippis"));
  }

  @Test
  void should_return_courses() {
    QueryClient.query(ALL_COURSES_QUERY)
        .statusCode(200)
        .body(
            "data.courses[0].name",
            Matchers.is("Laajis"),
            "data.courses[1].name",
            Matchers.is("Kippis"));
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

  @Test
  void should_not_show_deleted_course() throws InterruptedException {
    sendRecords(createRecord(Action.DELETE, 2, "Kippis"));

    QueryClient.query(ALL_COURSES_QUERY)
        .statusCode(200)
        .body("data.courses.size()", Matchers.is(1));
  }

  private ProducerRecord<Long, CourseEvent> createRecord(Action action, long id, String name) {
    var inputTopic = topology.description().input();
    var payload = new CoursePayload(id, name, List.of(new Hole(111l, 1, 4, 120)));
    return new ProducerRecord<>(inputTopic.name(), id, new CourseEvent(action, payload));
  }

  @SafeVarargs
  private void sendRecords(ProducerRecord<Long, CourseEvent>... records) throws InterruptedException {
    var inputTopic = topology.description().input();
    kafka.produce(inputTopic.keySerde(), inputTopic.valueSerde()).fromRecords(records);
    Thread.sleep(5000);
  }

}
