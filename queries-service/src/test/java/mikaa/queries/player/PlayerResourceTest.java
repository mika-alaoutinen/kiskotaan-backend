package mikaa.queries.player;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import mikaa.queries.dto.PlayerDTO;

import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.given;

@QuarkusTest
class PlayerResourceTest {

  private static final String ENDPOINT = "/players";

  @Test
  void should_get_all_players() {
    given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("$.size()", is(5));
  }

  @Test
  void should_get_player_by_id() {
    var response = given()
        .when()
        .get(ENDPOINT + "/2")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .extract()
        .as(PlayerDTO.class);

    var expected = new PlayerDTO(2, "Iines", "Ankka");
    assertPlayer(response, expected);
  }

  @Test
  void should_handle_player_not_found() {
    given()
        .when()
        .get(ENDPOINT + "/99")
        .then()
        .statusCode(404)
        .contentType(ContentType.JSON);
  }

  private static void assertPlayer(PlayerDTO player, PlayerDTO expected) {
    assertEquals(player.id(), expected.id());
    assertEquals(player.firstName(), expected.firstName());
    assertEquals(player.lastName(), expected.lastName());
  }

}
