package mikaa.feature.score;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.player.PlayerFinder;
import mikaa.feature.scorecard.ScoreCardService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ScoreResourceTest {

  private static final String ENDPOINT = "/scores";

  @InjectMock
  private ScoreCardService scoreCardService;

  @InjectMock
  private PlayerFinder playerFinder;

  @InjectMock
  private ScoreRepository repository;

  @Test
  void should_get_score_by_id() {
    var player = new PlayerEntity(222, "Pekka", "Kana");
    var score = new ScoreEntity(111L, 8, 4, player, null);
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(score));

    given()
        .when()
        .get(ENDPOINT + "/111")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "id", is(111),
            "playerId", is(222),
            "hole", is(8),
            "score", is(4));
  }

  @Test
  void should_throw_404_when_score_not_found() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.empty());

    given()
        .when()
        .get(ENDPOINT + "/111")
        .then()
        .statusCode(404)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(404),
            "error", is("Not Found"),
            "message", is("Could not find score with id 111"),
            "path", containsString("/scores/1"));
  }

  @Test
  void should_delete_score() {
    given()
        .when()
        .delete(ENDPOINT + "/1")
        .then()
        .statusCode(204);

    verify(repository, atLeastOnce()).deleteById(1L);
  }

}
