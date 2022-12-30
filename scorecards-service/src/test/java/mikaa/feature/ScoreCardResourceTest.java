package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import mikaa.model.NewScoreCardDTO;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@QuarkusTest
class ScoreCardResourceTest {

  private static final String ENDPOINT = "/scorecards";

  @InjectMock
  private ScoreCardRepository repository;

  @Test
  void should_get_all_score_cards() {
    var scoreCard = scoreCardMock();
    when(repository.listAll()).thenReturn(List.of(scoreCard));

    given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "[0].id", is(1),
            "[0].course.holes", is(18));
  }

  @Test
  void should_get_score_card_by_id() {
    var scoreCard = scoreCardMock();
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(scoreCard));

    given()
        .when()
        .get(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "id", is(1),
            "course.holes", is(18),
            "playerIds", hasItem(123),
            "scores[0].hole", is(1),
            "scores[0].score", is(3));
  }

  @Test
  void get_score_card_returns_404() {
    var response = given()
        .when()
        .get(ENDPOINT + "/1")
        .then();

    assertNotFoundResponse(response, 1);
  }

  @Test
  void should_add_new_score_card() {
    var newScoreCard = new NewScoreCardDTO()
        .courseId(BigDecimal.valueOf(1))
        .playersIds(Set.of(BigDecimal.valueOf(2), BigDecimal.valueOf(3)));

    given()
        .contentType(ContentType.JSON)
        .body(newScoreCard)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(201)
        .contentType(ContentType.JSON)
        .body(
            "id", notNullValue(),
            "course.id", is(1),
            "course.holes", is(18),
            "playerIds", hasItems(2, 3),
            "scores", empty());

    verify(repository, atLeastOnce()).persist(any(ScoreCardEntity.class));
  }

  private static void assertNotFoundResponse(ValidatableResponse response, int id) {
    response.statusCode(404)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(404),
            "error", is("Not Found"),
            "message", is("Could not find score card with id " + id),
            "path", containsString("/api/scorecards/" + id));
  }

  private static ScoreCardEntity scoreCardMock() {
    var course = new CourseEntity();
    course.setHoles(18);
    var score = new ScoreEntity(2L, 123L, 1, 3, null);
    return new ScoreCardEntity(1L, course, List.of(123L), List.of(score));
  }

}
