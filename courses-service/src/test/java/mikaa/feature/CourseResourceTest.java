package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import mikaa.dto.CourseNameDTO;
import mikaa.dto.NewCourseDTO;
import mikaa.dto.NewHoleDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static io.restassured.RestAssured.given;

import java.util.List;
import java.util.Optional;

@QuarkusTest
class CourseResourceTest {

  private static final String ENDPOINT = "/courses";

  @InjectMock
  private CourseRepository repository;

  @Test
  void should_get_all_courses() {
    var course = courseMock();
    when(repository.listAll()).thenReturn(List.of(course));

    given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "[0].id", is(1),
            "[0].name", is("DG Course"),
            "[0].holes", is(2),
            "[0].par", is(7));
  }

  @Test
  void should_get_course_by_id() {
    var course = courseMock();
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(course));

    given()
        .when()
        .get(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body(
            "id", is(1),
            "name", is("DG Course"),
            "holes.size()", is(2));
  }

  @Test
  void get_returns_404() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.empty());

    given()
        .when()
        .get(ENDPOINT + "/1")
        .then()
        .statusCode(404)
        .contentType(ContentType.JSON)
        .body(
          "timestamp", notNullValue(),
          "status", is(404),
          "error", is("Not Found"),
          "message", is("Could not find course with id 1"),
          "path", is("/courses/1"));
  }

  @Test
  void should_add_new_course() {
    var holes = List.of(new NewHoleDTO(1, 3, 85));
    var newCourse = new NewCourseDTO("New Course", holes);

    given()
        .contentType(ContentType.JSON)
        .body(newCourse)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(201)
        .contentType(ContentType.JSON)
        .body(
            "name", is("New Course"),
            "holes.size()", is(1));

    verify(repository, atLeastOnce()).persist(any(CourseEntity.class));
  }

  @Test
  void should_update_course_name() {
    when(repository.findByIdOptional(anyLong()))
        .thenReturn(Optional.of(new CourseEntity(1L, "Course 1", List.of())));

    given()
        .contentType(ContentType.JSON)
        .body(new CourseNameDTO("Updated name"))
        .when()
        .patch(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("name", is("Updated name"));
  }

  private static CourseEntity courseMock() {
    var holes = List.of(
        new HoleEntity(1L, 1, 3, 80, null),
        new HoleEntity(2L, 2, 4, 120, null));

    return new CourseEntity(1L, "DG Course", holes);
  }

}
