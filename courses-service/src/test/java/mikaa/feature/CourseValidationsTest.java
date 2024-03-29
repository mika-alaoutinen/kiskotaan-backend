package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import mikaa.model.HoleDTO;
import mikaa.model.NewCourseDTO;
import mikaa.model.NewCourseNameDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class CourseValidationsTest {

  static final String ENDPOINT = "/courses";

  @InjectMock
  CourseRepository repository;

  @Test
  void should_reject_new_course_without_holes() {
    var invalidCourse = new NewCourseDTO().name("New Course");

    var response = postInvalidCourse(invalidCourse);
    assertBadRequest(response, "newCourseDTO.holes", "size must be 1-30");

    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void should_reject_new_course_with_duplicate_name() {
    when(repository.existsByName(anyString())).thenReturn(true);

    var hole = new HoleDTO().number(1).par(3).distance(100);
    var invalidCourse = new NewCourseDTO().name("Duplicate name").holes(List.of(hole));
    var response = postInvalidCourse(invalidCourse);

    assertBadRequest(response, "course.name", "Course name should be unique");
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void should_reject_invalid_course_name_on_update() {
    var response = patchInvalidCourseName(new NewCourseNameDTO().name("A"));
    assertBadRequest(response, "newCourseNameDTO.name", "size must be 3-40");
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void should_reject_duplicate_course_name_on_update() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(courseMock()));
    when(repository.existsByName(anyString())).thenReturn(true);

    var response = patchInvalidCourseName(new NewCourseNameDTO().name("Duplicate name"));
    assertBadRequest(response, "course.name", "Course name should be unique");
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  private ValidatableResponse postInvalidCourse(NewCourseDTO invalidCourse) {
    return given()
        .contentType(ContentType.JSON)
        .body(invalidCourse)
        .when()
        .post(ENDPOINT)
        .then();
  }

  private ValidatableResponse patchInvalidCourseName(NewCourseNameDTO invalidName) {
    return given()
        .contentType(ContentType.JSON)
        .body(invalidName)
        .when()
        .patch(ENDPOINT + "/1")
        .then();
  }

  private static void assertBadRequest(
      ValidatableResponse res, String expectedField, String expectedMessage) {
    res.statusCode(400)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(400),
            "error", is("Bad Request"),
            "path", containsString("/courses"),
            "validationErrors[0].field", is(expectedField),
            "validationErrors[0].message", is(expectedMessage));
  }

  private static CourseEntity courseMock() {
    var course = new CourseEntity(1L, "DG Course", new ArrayList<>());
    course.addHole(HoleEntity.from(1, 3, 80));
    course.addHole(HoleEntity.from(2, 4, 120));
    return course;
  }

}
