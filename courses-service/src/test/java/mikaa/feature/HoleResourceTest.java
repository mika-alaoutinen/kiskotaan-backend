package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import mikaa.model.UpdatedHoleDTO;
import mikaa.producers.holes.HoleProducer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.verifyNoInteractions;
import static io.restassured.RestAssured.given;

@QuarkusTest
class HoleResourceTest {

  @InjectMock
  private HoleProducer producer;

  @Test
  void should_find_holes_for_a_course() {
    given()
        .when()
        .get("/courses/1/holes")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("$.size()", is(18));
  }

  @Test
  void should_return_empty_list_if_course_not_found() {
    given()
        .when()
        .get("/courses/99/holes")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("$.size()", is(0));
  }

  @Test
  void should_find_hole_by_id() {
    given()
        .when()
        .get(endpoint(1, 1))
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "number", is(1),
            "par", is(3),
            "distance", is(107));
  }

  @Test
  void get_returns_404_when_course_not_found() {
    var response = given()
        .when()
        .get(endpoint(99, 1))
        .then();

    assertCourseNotFoundResponse(response, 99, 1);
  }

  @Test
  void get_returns_404_when_course_has_no_hole_with_given_number() {
    var response = given()
        .when()
        .get(endpoint(1, 99))
        .then();

    assertHoleNotFoundResponse(response, 1, 99);
  }

  @Test
  void should_reject_update_with_invalid_payload() {
    given()
        .contentType(ContentType.JSON)
        .body(new UpdatedHoleDTO().par(0).distance(120))
        .when()
        .put(endpoint(1, 1))
        .then()
        .statusCode(400);

    verifyNoInteractions(producer);
  }

  @Test
  void put_returns_404_when_course_not_found() {
    var response = given()
        .contentType(ContentType.JSON)
        .body(new UpdatedHoleDTO().par(4).distance(100))
        .when()
        .put(endpoint(99, 1))
        .then();

    assertCourseNotFoundResponse(response, 99, 1);
    verifyNoInteractions(producer);
  }

  @Test
  void put_returns_404_when_course_has_no_hole_with_given_number() {
    var response = given()
        .contentType(ContentType.JSON)
        .body(new UpdatedHoleDTO().par(4).distance(100))
        .when()
        .put(endpoint(1, 99))
        .then();

    assertHoleNotFoundResponse(response, 1, 99);
    verifyNoInteractions(producer);
  }

  @Test
  void should_do_nothing_on_delete_if_course_not_found() {
    given()
        .when()
        .delete(endpoint(99, 1))
        .then()
        .statusCode(204);

    verifyNoInteractions(producer);
  }

  @Test
  void should_do_nothing_on_delete_if_hole_not_found() {
    given()
        .when()
        .delete(endpoint(1, 99))
        .then()
        .statusCode(204);

    verifyNoInteractions(producer);
  }

  private static String endpoint(int courseId, int number) {
    return String.format("/courses/%s/holes/%s", courseId, number);
  }

  private static void assertCourseNotFoundResponse(ValidatableResponse response, int courseId, int number) {
    String message = "Could not find course with id " + courseId;
    String path = endpoint(courseId, number);
    assertNotFoundResponse(response, message, path);
  }

  private static void assertHoleNotFoundResponse(ValidatableResponse response, int courseId, int number) {
    String message = String.format("Course %s has no hole %s", courseId, number);
    String path = endpoint(courseId, number);
    assertNotFoundResponse(response, message, path);
  }

  private static void assertNotFoundResponse(ValidatableResponse response, String message, String path) {
    response.statusCode(404)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(404),
            "error", is("Not Found"),
            "message", is(message),
            "path", is(path));
  }

}
