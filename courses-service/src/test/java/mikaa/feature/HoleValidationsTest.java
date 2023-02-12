package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import mikaa.dto.NewHoleDTO;
import mikaa.errors.ValidationError;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Optional;

@QuarkusTest
class HoleValidationsTest {

  static final String ENDPOINT = "/courses/1/holes";

  @InjectMock
  CourseRepository courseRepository;

  @InjectMock
  HoleRepository holeRepository;

  @Test
  void should_reject_invalid_hole_number() {
    var response = postInvalidHole(new NewHoleDTO(0, 3, 120));
    assertBadRequest(response, new ValidationError("number", "must be greater than or equal to 1"));
  }

  @Test
  void should_not_add_hole_with_duplicate_number() {
    when(courseRepository.findByIdOptional(anyLong())).thenReturn(Optional.of(courseMock()));
    var response = postInvalidHole(new NewHoleDTO(1, 3, 120));
    assertBadRequest(response, new ValidationError("number", "Duplicate hole number"));
  }

  @Test
  void should_not_update_hole_with_duplicate_number() {
    HoleEntity hole = new HoleEntity(1L, 1, 3, 80, courseMock());
    when(holeRepository.findByIdOptional(anyLong())).thenReturn(Optional.of(hole));

    given()
        .contentType(ContentType.JSON)
        .body(new NewHoleDTO(1, 3, 120))
        .when()
        .put("holes/1")
        .then()
        .statusCode(400)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(400),
            "error", is("Bad Request"),
            "path", is("/holes/1"),
            "validationErrors[0].field", is("number"),
            "validationErrors[0].message", is("Duplicate hole number"));

    verify(holeRepository, never()).persist(any(HoleEntity.class));
  }

  private ValidatableResponse postInvalidHole(NewHoleDTO hole) {
    return given()
        .contentType(ContentType.JSON)
        .body(hole)
        .when()
        .post(ENDPOINT)
        .then();
  }

  private void assertBadRequest(ValidatableResponse res, ValidationError expected) {
    res.statusCode(400)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(400),
            "error", is("Bad Request"),
            "path", is("/courses/1/holes"),
            "validationErrors[0].field", is(expected.field()),
            "validationErrors[0].message", is(expected.message()));

    verify(holeRepository, never()).persist(any(HoleEntity.class));
  }

  private static CourseEntity courseMock() {
    var course = new CourseEntity(1L, "DG Course", new ArrayList<>());
    course.addHole(HoleEntity.from(1, 3, 80));
    course.addHole(HoleEntity.from(2, 4, 120));
    return course;
  }

}
