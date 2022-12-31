package mikaa.feature.scorecard;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.CourseRepository;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.player.PlayerRepository;
import mikaa.model.NewScoreCardDTO;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@QuarkusTest
class ScoreCardResourceTest {

  private static final String ENDPOINT = "/scorecards";
  private static final CourseEntity COURSE = new CourseEntity(321L, 18, null);
  private static final PlayerEntity PEKKA_KANA = new PlayerEntity(123L, "Pekka", "Kana", null);

  @InjectMock
  private ScoreCardRepository repository;

  @InjectMock
  private CourseRepository courseRepository;

  @InjectMock
  private PlayerRepository playerRepository;

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
            "players[0].id", is(123),
            "players[0].firstName", is("Pekka"),
            "players[0].lastName", is("Kana"),
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
    when(courseRepository.findByIdOptional(anyLong())).thenReturn(Optional.of(COURSE));
    when(playerRepository.findByIdOptional(anyLong())).thenReturn(Optional.of(PEKKA_KANA));
    
    var newScoreCard = new NewScoreCardDTO()
        .courseId(BigDecimal.valueOf(1))
        .playersIds(Set.of(BigDecimal.valueOf(123)));

    given()
        .contentType(ContentType.JSON)
        .body(newScoreCard)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(200) // see readme for problem description
        .contentType(ContentType.JSON)
        .body(
            "course.id", is(321),
            "course.holes", is(18),
            "players.size()", is(1),
            "players[0].id", is(123),
            "scores", empty());

    verify(repository, atLeastOnce()).persist(any(ScoreCardEntity.class));
  }

  @Test
  void post_score_card_should_throw_404_when_course_not_found() {
    when(courseRepository.findByIdOptional(anyLong())).thenReturn(Optional.empty());

    var newScoreCard = new NewScoreCardDTO()
        .courseId(BigDecimal.valueOf(1))
        .playersIds(Set.of(BigDecimal.valueOf(321)));

    given()
        .contentType(ContentType.JSON)
        .body(newScoreCard)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(404)
        .contentType(ContentType.JSON)
        .body("message", is("Could not find course with id 1"));
    
    verify(repository, never()).persist(any(ScoreCardEntity.class));
  }

  @Test
  void post_score_card_should_throw_404_when_player_not_found() {
    when(courseRepository.findByIdOptional(anyLong())).thenReturn(Optional.of(COURSE));
    when(playerRepository.findByIdOptional(anyLong())).thenReturn(Optional.empty());

    var newScoreCard = new NewScoreCardDTO()
        .courseId(BigDecimal.valueOf(1))
        .playersIds(Set.of(BigDecimal.valueOf(999)));

    given()
        .contentType(ContentType.JSON)
        .body(newScoreCard)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(404)
        .contentType(ContentType.JSON)
        .body("message", is("Could not find player with id 999"));
    
    verify(repository, never()).persist(any(ScoreCardEntity.class));
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
    var score = new ScoreEntity(2L, 123L, 1, 3, null);
    return new ScoreCardEntity(1L, COURSE, List.of(PEKKA_KANA), List.of(score));
  }

}
