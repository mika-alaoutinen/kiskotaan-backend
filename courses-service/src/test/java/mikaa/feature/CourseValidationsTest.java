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
class CourseValidationsTest {

  static final String ENDPOINT = "/courses";

  @InjectMock
  CourseRepository repository;

  @InjectMock
  HoleRepository holeRepository;

  @Test
  void should_reject_new_course_without_holes() {
    var invalidCourse = new NewCourseDTO("New Course", List.of());

    given()
        .contentType(ContentType.JSON)
        .body(invalidCourse)
        .when()
        .post(ENDPOINT)
        .then()
        .statusCode(400)
        .contentType(ContentType.JSON);

    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void should_reject_invalid_course_name() {
    given()
        .contentType(ContentType.JSON)
        .body(new NewCourseNameDTO(""))
        .when()
        .patch(ENDPOINT + "/1")
        .then()
        .statusCode(400)
        .contentType(ContentType.JSON);

    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void should_reject_invalid_hole() {
    given()
        .contentType(ContentType.JSON)
        .body(new NewHoleDTO(0, 3, 120))
        .when()
        .post(ENDPOINT + "/1/holes")
        .then()
        .statusCode(400)
        .contentType(ContentType.JSON);

    verify(holeRepository, never()).persist(any(HoleEntity.class));
  }

}
