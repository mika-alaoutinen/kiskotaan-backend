package mikaa.feature.scorecard;

import static org.mockito.Mockito.when;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.HoleEntity;
import mikaa.feature.player.PlayerEntity;

@QuarkusTest
class RoundResultsResourceTest {

  private static final String ENDPOINT = "/results";
  private static final CourseEntity COURSE = courseMock();
  private static final PlayerEntity PEKKA_KANA = new PlayerEntity(123L, "Pekka", "Kana");

  @InjectMock
  private ScoreCardRepository repository;

  @Test
  void should_get_all_round_results() {
    when(repository.streamAll()).thenReturn(Stream.of(scoreCardMock()));

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
    when(repository.findByIdOptional(1L)).thenReturn(Optional.of(scoreCardMock()));

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

  private static CourseEntity courseMock() {
    var holes = List.of(
        new HoleEntity(1, 4),
        new HoleEntity(2, 5),
        new HoleEntity(3, 3));

    return new CourseEntity(321L, holes, "Laajis");
  }

  private static ScoreCardEntity scoreCardMock() {
    var scores = List.of(
        new ScoreEntity(1, 3, PEKKA_KANA),
        new ScoreEntity(2, 5, PEKKA_KANA));

    return new ScoreCardEntity(1L, COURSE, Set.of(PEKKA_KANA), scores);
  }

}
