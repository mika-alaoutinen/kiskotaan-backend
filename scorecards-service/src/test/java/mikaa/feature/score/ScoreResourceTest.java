package mikaa.feature.score;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.player.PlayerFinder;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.feature.scorecard.ScoreCardFinder;
import mikaa.producers.ScoreCardProducer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ScoreResourceTest {

  private static final String ENDPOINT = "/scores";

  @InjectMock
  private ScoreCardFinder scoreCardFinder;

  @InjectMock
  private PlayerFinder playerFinder;

  @InjectMock
  private ScoreCardProducer producer;

  @InjectMock
  private ScoreRepository repository;

  @Test
  void should_get_score_by_id() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(scoreMock()));

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
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(scoreMock()));
    delete();
    verify(repository, atLeastOnce()).deleteById(1L);
    verify(producer, atLeastOnce()).scoreCardUpdated(any(ScoreCardEntity.class));
  }

  @Test
  void should_do_nothing_on_delete_if_score_not_found() {
    delete();
    verify(repository, never()).deleteById(anyLong());
    verifyNoInteractions(producer);
  }

  private static void delete() {
    given()
        .when()
        .delete(ENDPOINT + "/1")
        .then()
        .statusCode(204);
  }

  private static ScoreEntity scoreMock() {
    var player = new PlayerEntity(222, "Pekka", "Kana");
    var scoreCard = new ScoreCardEntity(1L, null, Set.of(player), new ArrayList<>());
    return new ScoreEntity(111L, 8, 4, player, scoreCard);
  }

}
