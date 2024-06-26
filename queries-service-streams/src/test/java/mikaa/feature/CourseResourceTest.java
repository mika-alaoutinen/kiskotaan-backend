package mikaa.feature;

import java.util.List;

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
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.player.PlayerEvent;
import mikaa.kiskotaan.player.PlayerPayload;
import mikaa.kiskotaan.course.CourseEvent;
import mikaa.kiskotaan.course.CoursePayload;
import mikaa.kiskotaan.course.Hole;
import mikaa.util.KafkaCompanionWrapper;
import mikaa.util.QueryClient;

import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
@TestInstance(Lifecycle.PER_CLASS)
class CourseResourceTest {

  private static final String ALL_COURSES_QUERY = """
      {
        courses {
          id
          name
          holes {
            number
            par
            distance
          }
        }
      }
      """;

  @InjectKafkaCompanion
  private KafkaCompanion kafka;

  @Inject
  private KafkaCompanionWrapper kafkaWrapper;

  @BeforeAll
  void sendKafkaEvents() throws InterruptedException {
    kafkaWrapper.init(kafka);

    var aku = new PlayerPayload(1L, "Aku", "Ankka");
    kafkaWrapper.sendPlayer(new PlayerEvent(Action.ADD, aku));

    var laajis = new CoursePayload(1L, "Laajis", List.of(new Hole(111l, 1, 4, 120)));
    var kippis = new CoursePayload(2L, "Kippis", List.of());
    kafkaWrapper.sendCourse(new CourseEvent(Action.ADD, laajis));
    kafkaWrapper.sendCourse(new CourseEvent(Action.ADD, kippis));
  }

  @Test
  void should_return_courses() {
    QueryClient.query(ALL_COURSES_QUERY)
        .statusCode(200)
        .body(
            "data.courses[0].name", is("Laajis"),
            "data.courses[1].name", is("Kippis"));
  }

  @Test
  void should_return_course_by_id() {
    String query = """
        {
          course(id: 1) {
            id
            name
            holes {
              number
              par
              distance
            }
          }
        }
        """;

    QueryClient.query(query)
        .statusCode(200)
        .body("data.course.name", is("Laajis"),
            "data.course.holes[0].number", is(1),
            "data.course.holes[0].par", is(4),
            "data.course.holes[0].distance", is(120));
  }

  @Test
  void should_not_show_deleted_course() throws InterruptedException {
    var kippis = new CoursePayload(1L, "Laajis", List.of());
    kafkaWrapper.sendCourse(new CourseEvent(Action.DELETE, kippis));

    QueryClient.query(ALL_COURSES_QUERY)
        .statusCode(200)
        .body("data.courses.size()", is(1));
  }

}
