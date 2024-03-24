package mikaa.feature;

import static org.mockito.Mockito.verifyNoInteractions;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import mikaa.domain.NewPlayer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static io.restassured.RestAssured.given;

@QuarkusTest
class PlayerResourceTest {

  private static final String ENDPOINT = "/players";

  @InjectMock
  private PlayerProducer producer;

  @Test
  void should_get_all_players() {
    given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("$.size()", is(3));
  }

  @Test
  void should_get_player_by_id() {
    given()
        .when()
        .get(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "$.id", is(1L),
            "$.firstName", is("Aku"),
            "$.lastName", is("Ankka"));
  }

  @Test
  void get_should_return_404_when_player_not_found() {
    var response = given()
        .when()
        .get(ENDPOINT + "/99")
        .then();

    assertNotFoundResponse(response, 99);
  }

  @MethodSource("invalidNewPlayers")
  @NullSource
  @ParameterizedTest
  void should_not_add_player_if_malformed_payload(NewPlayer invalidPlayer) {
    var response = postPlayer(invalidPlayer);
    assertBadRequestResponse(response);
    verifyNoInteractions(producer);
  }

  @Test
  void should_not_add_player_if_name_not_unique() {
    var response = postPlayer(new NewPlayer("Aku", "Ankka"));
    assertBadRequestResponse(response);
    verifyNoInteractions(producer);
  }

  @Test
  void put_should_return_404_when_player_not_found() {
    var response = putPlayer(new NewPlayer("Updated", "Name"), 99);
    assertNotFoundResponse(response, 99);
    verifyNoInteractions(producer);
  }

  @MethodSource("invalidNewPlayers")
  @NullSource
  @ParameterizedTest
  void should_not_update_player_if_malformed_payload(NewPlayer invalidPlayer) {
    var response = putPlayer(invalidPlayer, 1);
    assertBadRequestResponse(response);
    verifyNoInteractions(producer);
  }

  @Test
  void should_not_update_player_if_name_not_unique() {
    var response = putPlayer(new NewPlayer("Aku", "Ankka"), 2);
    assertNotFoundResponse(response, 99);
    verifyNoInteractions(producer);
  }

  @Test
  void should_do_nothing_on_delete_when_id_not_found() {
    deletePlayer(99);
    verifyNoInteractions(producer);
  }

  private ValidatableResponse deletePlayer(long id) {
    return given()
        .when()
        .delete(ENDPOINT + "/" + id)
        .then()
        .statusCode(204);
  }

  private ValidatableResponse postPlayer(NewPlayer newPlayer) {
    return given()
        .contentType(ContentType.JSON)
        .body(newPlayer)
        .when()
        .post(ENDPOINT)
        .then();
  }

  private ValidatableResponse putPlayer(NewPlayer newPlayer, long id) {
    return given()
        .contentType(ContentType.JSON)
        .body(newPlayer)
        .when()
        .put(ENDPOINT + "/" + id)
        .then();
  }

  private static void assertBadRequestResponse(ValidatableResponse response) {
    response.statusCode(400)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(400),
            "error", is("Bad Request"),
            "path", containsString("/players"));
  }

  private static void assertNotFoundResponse(ValidatableResponse response, int id) {
    response.statusCode(404)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(404),
            "error", is("Not Found"),
            "message", is("Could not find player with id " + id),
            "path", containsString("/players/" + id));
  }

  private static Stream<NewPlayer> invalidNewPlayers() {
    return Stream.of(
        new NewPlayer("Kalle", null),
        new NewPlayer("Kalle", ""));
  }

}
