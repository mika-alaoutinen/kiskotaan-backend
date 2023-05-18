package mikaa.it;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.kiskotaan.domain.CourseUpdated;
import mikaa.kiskotaan.domain.HolePayload;
import mikaa.model.NewCourseDTO;
import mikaa.model.NewCourseNameDTO;
import mikaa.model.NewHoleDTO;
import mikaa.producers.courses.CourseProducer;
import mikaa.producers.holes.HoleProducer;

import static org.hamcrest.CoreMatchers.is;
import static io.restassured.RestAssured.given;

@QuarkusTest
class CoursesIT {

  private static final String ENDPOINT = "/courses";

  @InjectMock
  private CourseProducer courseProducer;

  @InjectMock
  private HoleProducer holeProducer;

  @Test
  void should_add_new_course() {
    var holes = List.of(new NewHoleDTO().number(1).par(3).distance(85));
    var newCourse = new NewCourseDTO().name("New Course").holes(holes);

    given()
        .contentType(ContentType.JSON)
        .body(newCourse)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "name", is("New Course"),
            "holes.size()", is(1));

    verify(courseProducer, atLeastOnce()).courseAdded(any(CoursePayload.class));
  }

  @Test
  void should_add_hole_for_a_course() {
    given()
        .contentType(ContentType.JSON)
        .body(new NewHoleDTO().number(3).par(3).distance(90))
        .when()
        .post(ENDPOINT + "/3/holes")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "number", is(3),
            "par", is(3),
            "distance", is(90));

    verify(holeProducer, atLeastOnce()).holeAdded(any(HolePayload.class));
  }

  @Test
  void should_update_course_name() {
    given()
        .contentType(ContentType.JSON)
        .body(new NewCourseNameDTO().name("Updated name"))
        .when()
        .patch(ENDPOINT + "/3")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("name", is("Updated name"));

    verify(courseProducer, atLeastOnce()).courseUpdated(any(CourseUpdated.class));
  }

  @Test
  void should_delete_course() {
    given()
        .when()
        .delete(ENDPOINT + "/4")
        .then()
        .statusCode(204);

    verify(courseProducer, atLeastOnce()).courseDeleted(any(CoursePayload.class));
  }

}
