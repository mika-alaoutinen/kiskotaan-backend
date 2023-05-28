package mikaa.queries.scorecard;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.given;

@QuarkusTest
class ScoreCardsResourceTest {

  private static final String ENDPOINT = "/scorecards";

  @Test
  void should_get_all_score_cards() {
    given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "$.size()", is(1));
  }

  @Test
  void should_get_score_card_by_id() {
    given()
        .when()
        .get(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "id", is(1),

            "course.id", is(1),
            "course.name", is("Frisbeegolf Laajis"),
            "course.holes.size()", is(18),
            "course.holes[17].number", is(18),
            "course.holes[17].par", is(3),
            "course.holes[17].distance", is(164),

            "players.size()", is(2),
            "players[1].id", is(2),
            "players[1].firstName", is("Iines"),
            "players[1].lastName", is("Ankka"));
  }

  @Test
  void should_handle_score_card_not_found() {
    given()
        .when()
        .get(ENDPOINT + "/99")
        .then()
        .statusCode(404)
        .contentType(ContentType.JSON);
  }

}
