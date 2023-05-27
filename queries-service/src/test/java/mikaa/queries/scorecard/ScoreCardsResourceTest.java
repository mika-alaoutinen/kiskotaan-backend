package mikaa.queries.scorecard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ScoreCardsResourceTest {

  private static final String ENDPOINT = "/scorecards";

  @Test
  void should_get_all_score_cards() {
    var response = given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .extract()
        .as(ScoreCardDTO[].class);

    assertEquals(1, response.length);
  }

  @Test
  void should_get_score_card_by_id() {
    var response = given()
        .when()
        .get(ENDPOINT + "/3")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .extract()
        .as(ScoreCardDTO.class);
  }

  @Disabled("Mock data never returns 404")
  @Test
  void should_handle_score_card_not_found() {
    given()
        .when()
        .get(ENDPOINT + "/99")
        .then()
        .statusCode(404)
        .contentType(ContentType.JSON);
  }

  private static void assertScoreCard(ScoreCardDTO scoreCard, ScoreCardDTO expected) {
    assertEquals(scoreCard.id(), expected.id());
    assertEquals(scoreCard.course().name(), expected.course().name());
    assertEquals(scoreCard.players().size(), expected.players().size());
  }

}
