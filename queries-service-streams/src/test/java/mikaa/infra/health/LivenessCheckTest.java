package mikaa.infra.health;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class LivenessCheckTest {

  @Test
  void status_down_when_streams_not_started() {
    given()
        .when()
        .get("/q/health/live")
        .then()
        .statusCode(503)
        .body("status", is("DOWN"));
  }

}
