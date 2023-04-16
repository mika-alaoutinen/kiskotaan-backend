package mikaa.feature.player;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;

@QuarkusTest
class PlayerResourceTest {

  private static final String ENDPOINT = "/players";

  @Test
  void should_get_all_courses() {
    given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(500);
  }

}
