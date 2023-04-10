package mikaa.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;

import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static io.restassured.RestAssured.given;

import java.util.List;
import java.util.stream.Stream;

@QuarkusTest
class CoursesFilteringTest {

  @InjectMock
  private CourseRepository repository;

  @BeforeEach
  void setup() {
    var holes = List.of(
        new HoleEntity(1L, 1, 3, 80, null),
        new HoleEntity(2L, 2, 4, 100, null),
        new HoleEntity(3L, 3, 5, 150, null));

    var course = new CourseEntity(1L, "Test course 1", holes);

    when(repository.streamAll()).thenReturn(Stream.of(course));
  }

  @Test
  void should_find_one_course() {
    given()
        .when()
        .get(url("Test", 1, 18, 1, 90))
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("$.size()", is(1));
  }

  @Test
  void should_filter_out_course() {
    given()
        .when()
        .get(url("Test", 1, 2, 1, 90))
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("$.size()", is(0));
  }

  @Test
  void filters_are_optional() {
    given()
        .when()
        .get("/courses?name=test")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("$.size()", is(1));
  }

  private static String url(String name, int holesMin, int holesMax, int parMin, int parMax) {
    return String.format(
        "/courses?name=%s&holesMin=%d&holesMax=%d&parMin=%d&parMax=%d",
        name, holesMin, holesMax, parMin, parMax);
  }

}
