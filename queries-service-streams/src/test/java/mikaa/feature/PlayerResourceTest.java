package mikaa.feature;

import java.util.List;

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
import mikaa.kiskotaan.course.CourseEvent;
import mikaa.kiskotaan.course.CoursePayload;
import mikaa.kiskotaan.course.Hole;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.player.PlayerEvent;
import mikaa.kiskotaan.player.PlayerPayload;
import mikaa.util.KafkaCompanionWrapper;
import mikaa.util.QueryClient;

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
@TestInstance(Lifecycle.PER_CLASS)
class PlayerResourceTest {

  private static final String ALL_PLAYERS_QUERY = """
      {
        players {
          id
          firstName
          lastName
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
    var iines = new PlayerPayload(2L, "Iines", "Ankka");
    kafkaWrapper.sendPlayer(new PlayerEvent(Action.ADD, aku));
    kafkaWrapper.sendPlayer(new PlayerEvent(Action.ADD, iines));

    var laajis = new CoursePayload(1L, "Laajis", List.of(new Hole(111l, 1, 4, 120)));
    kafkaWrapper.sendCourse(new CourseEvent(Action.ADD, laajis));
  }

  @Test
  void should_return_players() {
    QueryClient.query(ALL_PLAYERS_QUERY)
        .statusCode(200)
        .body(
            "data.players[0].firstName",
            Matchers.is("Aku"),
            "data.players[0].lastName",
            Matchers.is("Ankka"),
            "data.players[1].firstName",
            Matchers.is("Iines"),
            "data.players[1].lastName",
            Matchers.is("Ankka"));
  }

  @Test
  void should_return_player_by_id() {
    String query = """
        {
          player(id: 1) {
            id
            firstName
          }
        }
        """;

    QueryClient.query(query)
        .statusCode(200)
        .body("data.player.firstName", Matchers.is("Aku"));
  }

  @Test
  void should_not_show_deleted_player() throws InterruptedException {
    var iines = new PlayerPayload(2L, "Iines", "Ankka");
    kafkaWrapper.sendPlayer(new PlayerEvent(Action.DELETE, iines));

    QueryClient.query(ALL_PLAYERS_QUERY)
        .statusCode(200)
        .body("data.players.size()", Matchers.is(1));
  }

}
