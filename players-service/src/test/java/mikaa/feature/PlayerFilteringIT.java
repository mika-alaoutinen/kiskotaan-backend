package mikaa.feature;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

@QuarkusTest
class PlayerFilteringIT {

  @ParameterizedTest
  @ValueSource(strings = { "Hessu", "essu", "hes", "opo" })
  void should_filter_players_by_first_name_or_last_name() {
    doGet("Hessu")
        .body(
            "$.size()", is(1),
            "[0].firstName", is("Hessu"));
  }

  @ParameterizedTest
  @ValueSource(strings = { "Hessu Hopo", "ess opo" })
  void should_filter_players_by_first_name_and_last_name() {
    doGet("hes opo")
        .body(
            "$.size()", is(1),
            "[0].firstName", is("Hessu"));
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
