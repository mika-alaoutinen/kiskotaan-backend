package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import mikaa.domain.NewPlayer;
import mikaa.domain.Player;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static io.restassured.RestAssured.given;

@QuarkusTest
class PlayerIT {

  private static final String ENDPOINT = "/players";

  @InjectMock
  private PlayerProducer producer;

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

    verify(producer, atLeastOnce()).playerAdded(any(Player.class));
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

    verify(producer, atLeastOnce()).playerUpdated(any(Player.class));
  }

  @Test
  void should_delete_player_by_id() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .delete(ENDPOINT + "/3")
        .then()
        .statusCode(204);

    verify(producer, atLeastOnce()).playerDeleted(any(Player.class));
  }

}
