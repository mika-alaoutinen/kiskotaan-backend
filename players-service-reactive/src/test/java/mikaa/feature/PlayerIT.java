package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import mikaa.domain.NewPlayer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import static io.restassured.RestAssured.given;

@QuarkusTest
class PlayerIT {

  private static final String ENDPOINT = "/players";

  @Test
  void should_add_new_player() {
    given()
        .contentType(ContentType.JSON)
        .body(new NewPlayer("New", "Player"))
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(200)
        .body(
            "id", notNullValue(),
            "firstName", is("New"),
            "lastName", is("Player"));
  }

  @Test
  void should_update_player() {
    given()
        .contentType(ContentType.JSON)
        .body(new NewPlayer("Iines", "Metso"))
        .when()
        .put(ENDPOINT + "/2")
        .then()
        .statusCode(200)
        .body(
            "id", notNullValue(),
            "firstName", is("Iines"),
            "lastName", is("Metso"));
  }

  @Test
  void should_delete_player_by_id() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .delete(ENDPOINT + "/3")
        .then()
        .statusCode(204);
  }

}
