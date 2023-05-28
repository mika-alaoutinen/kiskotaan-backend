package mikaa.feature.score;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jakarta.ws.rs.NotFoundException;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import mikaa.kiskotaan.domain.ScorePayload;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.player.PlayerFinder;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.feature.scorecard.ScoreCardFinder;
import mikaa.model.NewScoreDTO;
import mikaa.producers.score.ScoreProducer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.never;

@QuarkusTest
class AddScoreResourceTest {

  private static final PlayerEntity PEKKA_KANA = new PlayerEntity(123L, "Pekka", "Kana");
  private static final NewScoreDTO NEW_SCORE = new NewScoreDTO()
      .hole(18)
      .playerId(new BigDecimal("123"))
      .score(3);

  @InjectMock
  private ScoreCardFinder scoreCardFinder;

  @InjectMock
  private PlayerFinder playerFinder;

  @InjectMock
  private ScoreProducer producer;

  @InjectMock
  private ScoreRepository repository;

  @Test
  void should_add_new_score() {
    when(scoreCardFinder.findOrThrow(anyLong())).thenReturn(scoreCardMock());
    when(playerFinder.findOrThrow(anyLong())).thenReturn(PEKKA_KANA);

    postScore(NEW_SCORE)
        .statusCode(200) // see readme for problem description
        .contentType(ContentType.JSON)
        .body(
            "playerId", is(123),
            "hole", is(18),
            "score", is(3));

    verify(repository, atLeastOnce()).persist(any(ScoreEntity.class));
    verify(producer, atLeastOnce()).scoreAdded(any(ScorePayload.class));
  }

  @Test
  void should_throw_400_when_invalid_request_body() {
    when(scoreCardFinder.findOrThrow(anyLong())).thenReturn(scoreCardMock());
    when(playerFinder.findOrThrow(anyLong())).thenReturn(PEKKA_KANA);

    var invalidScore = new NewScoreDTO()
        .playerId(new BigDecimal("123"))
        .score(3);

    String path = getEndpointUrl(1);
    
    postScore(invalidScore)
        .statusCode(400)
        .contentType(ContentType.JSON)
        .body(
          "timestamp", notNullValue(),
          "status", is(400),
          "error", is("Bad Request"),
          "message", is("Invalid request body"),
          "path", containsString(path));

    verify(repository, never()).persist(any(ScoreEntity.class));
    verify(producer, never()).scoreAdded(any(ScorePayload.class));
  }

  @Test
  void should_throw_404_when_scorecard_not_found() {
    String errorMsg = "Could not find score card with id 1";
    when(scoreCardFinder.findOrThrow(anyLong())).thenThrow(new NotFoundException(errorMsg));

    var response = postScore(NEW_SCORE);
    assertNotFoundResponse(response, errorMsg, 1);

    verify(repository, never()).persist(any(ScoreEntity.class));
    verify(producer, never()).scoreAdded(any(ScorePayload.class));
  }

  @Test
  void should_throw_404_when_player_not_found() {
    String errorMsg = "Could not find player with id 123";
    when(playerFinder.findOrThrow(anyLong())).thenThrow(new NotFoundException(errorMsg));

    var response = postScore(NEW_SCORE);
    assertNotFoundResponse(response, errorMsg, 1);

    verify(repository, never()).persist(any(ScoreEntity.class));
    verify(producer, never()).scoreAdded(any(ScorePayload.class));
  }

  private static void assertNotFoundResponse(ValidatableResponse response, String message, int scoreCardId) {
    String path = getEndpointUrl(scoreCardId);

    response.statusCode(404)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(404),
            "error", is("Not Found"),
            "message", is(message),
            "path", containsString(path));
  }

  private static ValidatableResponse postScore(NewScoreDTO score) {
    return given()
        .contentType(ContentType.JSON)
        .body(score)
        .when()
        .post(getEndpointUrl(1))
        .then();
  }

  private static String getEndpointUrl(int id) {
    return String.format("scorecards/%d/scores", id);
  }

  private static ScoreCardEntity scoreCardMock() {
    var course = new CourseEntity(321L, 18, "Laajis", 59);
    var players = new HashSet<>(List.of(PEKKA_KANA));
    var scores = new ArrayList<ScoreEntity>(List.of(new ScoreEntity(2L, 1, 3, PEKKA_KANA, null)));
    return new ScoreCardEntity(1L, course, players, scores);
  }

}
