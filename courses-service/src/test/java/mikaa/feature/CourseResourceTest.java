package mikaa.feature;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import mikaa.dto.NewCourseDTO;
import mikaa.dto.NewHoleDTO;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static io.restassured.RestAssured.given;

import java.util.List;
import java.util.Optional;

@QuarkusTest
class CourseResourceTest {

  private static final String ENDPOINT = "/courses";

  @Test
  void should_get_all_courses() {
    var course = courseMock();
    when(CourseEntity.listAll()).thenReturn(List.of(course));

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
    when(CourseEntity.findByIdOptional(anyLong())).thenReturn(Optional.of(course));

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
    PanacheMock.mock(CourseEntity.class);
    when(CourseEntity.findByIdOptional(anyLong())).thenReturn(Optional.empty());

    given()
        .when()
        .get(ENDPOINT + "/1")
        .then()
        .statusCode(404);
  }

  @Disabled("Cannot get PanacheMock to work")
  @Test
  void should_add_new_course() {
    PanacheMock.mock(CourseEntity.class);

    var holes = List.of(new NewHoleDTO(1, 3, 85));
    var newCourse = new NewCourseDTO("New Course", holes);

    given()
        .when()
        .contentType(ContentType.JSON)
        .body(newCourse)
        .post(ENDPOINT)
        .then()
        .statusCode(201)
        .contentType(ContentType.JSON)
        .body("name", is("New Course"));
  }

  private static PanacheEntityBase courseMock() {
    PanacheMock.mock(CourseEntity.class);

    List<HoleEntity> holes = List.of(
        new HoleEntity(1, 1, 3, 80),
        new HoleEntity(2, 2, 4, 120));

    return new CourseEntity(1, "DG Course", holes);
  }
}
