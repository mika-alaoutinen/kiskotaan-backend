package mikaa.feature;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import mikaa.dto.NewCourseDTO;
import mikaa.dto.NewCourseNameDTO;
import mikaa.dto.NewHoleDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
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
class CourseResourceTest {

  private static final String ENDPOINT = "/courses";

  @InjectMock
  private CourseRepository repository;

  @InjectMock
  private HoleRepository holeRepository;

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

    var response = given()
        .when()
        .get(ENDPOINT + "/1")
        .then();

    assertNotFoundResponse(response, 1);
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
  void should_add_hole_for_a_course() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(courseMock()));
    
    given()
        .contentType(ContentType.JSON)
        .body(new NewHoleDTO(3, 3, 90))
        .when()
        .post(ENDPOINT + "/1/holes")
        .then()
        .statusCode(201)
        .contentType(ContentType.JSON)
        .body(
          "number", is(3),
          "par", is(3),
          "distance", is(90));

    verify(holeRepository, atLeastOnce()).persist(any(HoleEntity.class));
  }

  @Test
  void add_hole_returns_404_if_course_not_found() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.empty());

    var response = given()
        .contentType(ContentType.JSON)
        .body(new NewHoleDTO(2, 3, 120))
        .when()
        .post(ENDPOINT + "/1/holes")
        .then();

    assertNotFoundResponse(response, 1);
    verify(holeRepository, never()).persist(any(HoleEntity.class));
  }

  @Test
  void should_update_course_name() {
    when(repository.findByIdOptional(anyLong()))
        .thenReturn(Optional.of(new CourseEntity(1L, "Course 1", List.of())));

    given()
        .contentType(ContentType.JSON)
        .body(new NewCourseNameDTO("Updated name"))
        .when()
        .patch(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("name", is("Updated name"));
  }

  @Test
  void patch_returns_404() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.empty());

    var response = given()
        .contentType(ContentType.JSON)
        .body(new NewCourseNameDTO("Updated name"))
        .when()
        .patch(ENDPOINT + "/1")
        .then();
    
    assertNotFoundResponse(response, 1);
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void should_delete_course() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(courseMock()));
    
    given()
        .when()
        .delete(ENDPOINT + "/1")
        .then()
        .statusCode(204);

    verify(repository, atLeastOnce()).deleteById(1L);
  }

  private static void assertNotFoundResponse(ValidatableResponse response, int id) {
    response.statusCode(404)
        .contentType(ContentType.JSON)
        .body(
            "timestamp", notNullValue(),
            "status", is(404),
            "error", is("Not Found"),
            "message", is("Could not find course with id " + id),
            "path", containsString("/api/courses/" + id));
  }

  private static CourseEntity courseMock() {
    var course = new CourseEntity(1L, "DG Course", new ArrayList<>());
    course.addHole(HoleEntity.from(1, 3, 80));
    course.addHole(HoleEntity.from(2, 4, 120));
    return course;
  }

}
