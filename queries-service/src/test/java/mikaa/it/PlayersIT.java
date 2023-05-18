package mikaa.it;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.PlayerPayload;
import mikaa.config.IncomingChannels;
import mikaa.queries.dto.PlayerDTO;

import static io.restassured.RestAssured.given;

@QuarkusTest
class PlayersIT {

  @Any
  @Inject
  InMemoryConnector connector;

  @Test
  void fetch_added_player() {
    var playerId = 10;
    fetchByIdAndExpectNotFound(playerId);

    var source = initSource(IncomingChannels.Player.PLAYER_ADDED);
    source.send(new PlayerPayload(playerId, "New", "Player"));

    var response = fetchById(playerId);
    assertPlayer(new PlayerDTO(playerId, "New", "Player"), response);
  }

  @Test
  void try_to_fetch_deleted_player() {
    var playerId = 101;
    fetchById(playerId);

    var source = initSource(IncomingChannels.Player.PLAYER_DELETED);
    source.send(new PlayerPayload(playerId, "Deleted", "Player"));

    fetchByIdAndExpectNotFound(playerId);
  }

  @Test
  void fetch_updated_player() {
    var playerId = 100;
    fetchById(playerId);

    var source = initSource(IncomingChannels.Player.PLAYER_UPDATED);
    source.send(new PlayerPayload(playerId, "Updated", "Player"));

    var response = fetchById(playerId);
    assertPlayer(new PlayerDTO(playerId, "Updated", "Player"), response);
  }

  private static void assertPlayer(PlayerDTO expected, PlayerDTO actual) {
    assertEquals(expected.id(), actual.id());
    assertEquals(expected.firstName(), actual.firstName());
    assertEquals(expected.lastName(), actual.lastName());
  }

  private PlayerDTO fetchById(int id) {
    return fetchByIdAndVerifyStatus(id, 200).extract().as(PlayerDTO.class);
  }

  private ValidatableResponse fetchByIdAndExpectNotFound(int id) {
    return fetchByIdAndVerifyStatus(id, 404);
  }

  private ValidatableResponse fetchByIdAndVerifyStatus(int id, int expectedStatus) {
    return given()
        .when()
        .get("/players/%d".formatted(id))
        .then()
        .statusCode(expectedStatus)
        .contentType(ContentType.JSON);
  }

  private InMemorySource<PlayerPayload> initSource(String channel) {
    return connector.source(channel);
  }

}
