package mikaa.feature;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

@QuarkusTest
class PlayerFilteringIT {

  @Test
  void should_filter_players_by_first_name_or_last_name() {
    doGet("Hessu")
        .body("$.size()", is(1), "firstName", "Hessu");
  }

  @Test
  void should_filter_players_by_first_name_and_last_name() {
    doGet("hes opo")
        .body("$.size()", is(1), "firstName", "Hessu");
  }

  @Test
  void should_find_aku_and_iines() {
    doGet("Ankka")
        .body("$.size()", is(2));
  }

  private static ValidatableResponse doGet(String nameFilter) {
    return given()
        .when()
        .get(String.format("/players?name=%s", nameFilter))
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON);
  }

}
