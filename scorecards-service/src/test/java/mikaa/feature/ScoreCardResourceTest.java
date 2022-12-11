package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ScoreCardResourceTest {

  private static final String ENDPOINT = "/scorecards";

  @Test
  void has_hello_world_endpoint() {
    given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .body(is("Hello world"));
  }

}
