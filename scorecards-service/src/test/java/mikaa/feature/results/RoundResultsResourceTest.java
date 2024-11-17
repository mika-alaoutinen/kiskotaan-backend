package mikaa.feature.results;

import static org.mockito.Mockito.when;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import mikaa.domain.Course;
import mikaa.domain.Hole;
import mikaa.domain.Player;
import mikaa.domain.Score;
import mikaa.domain.ScoreCard;
import mikaa.feature.scorecard.ScoreCardFinder;

@QuarkusTest
class RoundResultsResourceTest {

  private static final String ENDPOINT = "/results";

  @InjectMock
  private ScoreCardFinder scoreCardFinder;

  @Test
  void should_get_all_round_results() {
    when(scoreCardFinder.findAll()).thenReturn(List.of(scoreCardMock()));

    given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "[0].id", is(1),
            "[0].course.holes", is(3),
            "[0].course.name", is("Laajis"),
            "[0].course.par", is(12));
  }

  @Test
  void should_get_round_result_by_score_card_id() {
    when(scoreCardFinder.findByIdOrThrow(1L)).thenReturn(scoreCardMock());

    given()
        .when()
        .get(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "id", is(1),
            "course.holes", is(3),
            "course.name", is("Laajis"),
            "course.par", is(12));
  }

  private static ScoreCard scoreCardMock() {
    var holes = List.of(
        new Hole(1, 4),
        new Hole(2, 5),
        new Hole(3, 3));

    var course = new Course(321L, "Laajis", holes);
    var player = new Player(123L, "Pekka", "Kana");

    var scores = List.of(
        new Score(1, player.id(), 1, 3),
        new Score(2, player.id(), 2, 5));

    return new ScoreCard(1L, course, Set.of(player), scores);
  }

}
