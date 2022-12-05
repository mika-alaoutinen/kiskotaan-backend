package mikaa.feature;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class LoggingResourceTest {

  @Test
  void exposes_logging_settings_via_endpoint() {
    given()
        .when().get("/logging")
        .then()
        .statusCode(200)
        .body(is("Logging enabled, log level info"));
  }

}
