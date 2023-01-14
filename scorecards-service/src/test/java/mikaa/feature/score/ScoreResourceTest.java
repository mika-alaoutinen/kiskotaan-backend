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

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.player.PlayerService;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.feature.scorecard.ScoreCardService;
import mikaa.model.ScoreDTO;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ScoreResourceTest {

  private static final PlayerEntity PEKKA_KANA = new PlayerEntity(123L, "Pekka", "Kana");

  @InjectMock
  private ScoreCardService scoreCardService;

  @InjectMock
  private PlayerService playerService;

  @InjectMock
  private ScoreRepository repository;

  @Test
  void should_add_new_score() {
    when(scoreCardService.findOrThrow(anyLong())).thenReturn(scoreCardMock());
    when(playerService.findOrThrow(anyLong())).thenReturn(PEKKA_KANA);
    
    var newScore = new ScoreDTO()
        .hole(18)
        .playerId(new BigDecimal("123"))
        .score(3);

    given()
        .contentType(ContentType.JSON)
        .body(newScore)
        .when()
        .post(getEndpointUrl(1))
        .then()
        .statusCode(200) // see readme for problem description
        .contentType(ContentType.JSON)
        .body(
            "playerId", is(123),
            "hole", is(18),
            "score", is(3));

    verify(repository, atLeastOnce()).persist(any(ScoreEntity.class));
  }

  private static String getEndpointUrl(int id) {
    return String.format("/scorecards/%s/scores", id);
  }

  private static ScoreCardEntity scoreCardMock() {
    var course = new CourseEntity(321L, 18, "Laajis", null);
    var players = new HashSet<>(List.of(PEKKA_KANA));
    var scores = new ArrayList<ScoreEntity>(List.of(new ScoreEntity(2L, 1, 3, PEKKA_KANA, null)));
    return new ScoreCardEntity(1L, course, players, scores);
  }

}
