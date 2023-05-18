package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import mikaa.model.NewHoleDTO;
import mikaa.producers.holes.HoleProducer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.verifyNoInteractions;
import static io.restassured.RestAssured.given;

@QuarkusTest
class HoleResourceTest {

  private static final String ENDPOINT = "/holes";

  @InjectMock
  private HoleProducer producer;

  @Test
  void should_find_hole_by_id() {
    given()
        .when()
        .get(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "id", is(1),
            "number", is(1),
            "par", is(3),
            "distance", is(107));
  }

  @Test
  void get_returns_404() {
    var response = given()
        .when()
        .get(ENDPOINT + "/99")
        .then();

    assertNotFoundResponse(response, 99);
  }

  @Test
  void should_reject_update_with_invalid_payload() {
    given()
        .contentType(ContentType.JSON)
        .body(new NewHoleDTO().number(0).par(3).distance(120))
        .when()
        .put(ENDPOINT + "/1")
        .then()
        .statusCode(400);

    verifyNoInteractions(producer);
  }

  @Test
  void put_returns_404() {
    var response = given()
        .contentType(ContentType.JSON)
        .body(new NewHoleDTO().number(2).par(4).distance(100))
        .when()
        .put(ENDPOINT + "/99")
        .then();

    assertNotFoundResponse(response, 99);
    verifyNoInteractions(producer);
  }

  @Test
  void should_do_nothing_on_delete_if_hole_not_found() {
    given()
        .when()
        .delete(ENDPOINT + "/99")
        .then()
        .statusCode(204);

    verifyNoInteractions(producer);
  }

  private static void assertNotFoundResponse(ValidatableResponse response, int id) {
    response.statusCode(404)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(404),
            "error", is("Not Found"),
            "message", is("Could not find hole with id " + id),
            "path", is("/holes/" + id));
  }

}
