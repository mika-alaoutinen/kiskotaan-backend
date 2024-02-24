package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import mikaa.model.HoleDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static io.restassured.RestAssured.given;

import java.util.ArrayList;

@QuarkusTest
class HoleValidationsTest {

  static final String ENDPOINT = "/courses/1/holes";

  @InjectMock
  CourseFinder courseRepository;

  @InjectMock
  HoleRepository holeRepository;

  @Test
  void should_reject_invalid_hole_number() {
    var response = postInvalidHole(new HoleDTO().number(0).par(3).distance(120));
    assertBadRequest(response, "holeDTO.number", "must be greater than or equal to 1");
  }

  @Test
  void should_not_add_hole_with_duplicate_number() {
    when(courseRepository.findCourseOrThrow(anyLong())).thenReturn(courseMock());
    var response = postInvalidHole(new HoleDTO().number(1).par(3).distance(120));
    assertBadRequest(response, "hole.number", "Duplicate hole number");
  }

  private ValidatableResponse postInvalidHole(HoleDTO hole) {
    return given()
        .contentType(ContentType.JSON)
        .body(hole)
        .when()
        .post(ENDPOINT)
        .then();
  }

  private void assertBadRequest(ValidatableResponse res, String expectedField, String expectedMessage) {
    res.statusCode(400)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(400),
            "error", is("Bad Request"),
            "path", is("/courses/1/holes"),
            "validationErrors[0].field", is(expectedField),
            "validationErrors[0].message", is(expectedMessage));

    verify(holeRepository, never()).persist(any(HoleEntity.class));
  }

  private static CourseEntity courseMock() {
    var course = new CourseEntity(1L, "DG Course", new ArrayList<>());
    course.addHole(HoleEntity.from(1, 3, 80));
    course.addHole(HoleEntity.from(2, 4, 120));
    return course;
  }

}
