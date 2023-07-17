package mikaa.feature.course;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import mikaa.model.HoleDTO;
import mikaa.model.NewCourseNameDTO;
import mikaa.producers.courses.CourseProducer;
import mikaa.producers.holes.HoleProducer;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static io.restassured.RestAssured.given;

@QuarkusTest
class CourseResourceTest {

  private static final String ENDPOINT = "/courses";

  @InjectMock
  private CourseProducer courseProducer;

  @InjectMock
  private HoleProducer holeProducer;

  @Test
  void should_get_all_courses() {
    given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("$.size()", is(4));
  }

  @Test
  void should_get_course_by_id() {
    given()
        .when()
        .get(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "id", is(1),
            "name", is("Frisbeegolf Laajis"),
            "holes.size()", is(18));
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
  void add_hole_returns_404_if_course_not_found() {
    var response = given()
        .contentType(ContentType.JSON)
        .body(new HoleDTO().number(2).par(3).distance(120))
        .when()
        .post(ENDPOINT + "/99/holes")
        .then();

    assertNotFoundResponse(response, 99);
    verifyNoInteractions(holeProducer);
  }

  @Test
  void patch_returns_404() {
    var response = given()
        .contentType(ContentType.JSON)
        .body(new NewCourseNameDTO().name("Updated name"))
        .when()
        .patch(ENDPOINT + "/99")
        .then();

    assertNotFoundResponse(response, 99);
    verifyNoInteractions(courseProducer);
  }

  @Test
  void should_do_nothing_on_delete_if_course_not_found() {
    given()
        .when()
        .delete(ENDPOINT + "/99")
        .then()
        .statusCode(204);

    verifyNoInteractions(courseProducer);
  }

  private static void assertNotFoundResponse(ValidatableResponse response, int id) {
    response.statusCode(404)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(404),
            "error", is("Not Found"),
            "message", is("Could not find course with id " + id),
            "path", containsString("/courses/" + id));
  }

}
