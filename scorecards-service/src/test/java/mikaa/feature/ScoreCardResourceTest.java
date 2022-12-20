package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class ScoreCardResourceTest {

  private static final String ENDPOINT = "/scorecards";

  @Test
  void get_course_returns_404() {
    var response = given()
        .when()
        .get(ENDPOINT + "/1")
        .then();

    assertNotFoundResponse(response, 1);
  }

  private static void assertNotFoundResponse(ValidatableResponse response, int id) {
    response.statusCode(404)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(404),
            "error", is("Not Found"),
            "message", is("Could not find score card with id " + id),
            "path", containsString("/api/scorecards/" + id));
  }

}
