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

import javax.ws.rs.NotFoundException;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
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
  void should_delete_score() {
    given()
        .when()
        .delete("/scores/1")
        .then()
        .statusCode(204);

    verify(repository, atLeastOnce()).deleteById(1L);
  }

  private static void assertNotFoundResponse(ValidatableResponse response, String message, int scoreCardId) {
    response.statusCode(404)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(404),
            "error", is("Not Found"),
            "message", is(message),
            "path", containsString("/api/scores"));
  }

}
