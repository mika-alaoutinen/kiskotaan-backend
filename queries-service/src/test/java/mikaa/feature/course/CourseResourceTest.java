package mikaa.feature.course;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import mikaa.dto.CourseDTO;
import mikaa.dto.CourseSummaryDTO;
import mikaa.feature.TestData;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CourseResourceTest {

  private static final String ENDPOINT = "/courses";

  @Test
  void should_get_all_courses() {
    var response = given()
        .when()
        .get(ENDPOINT)
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .extract()
        .as(CourseSummaryDTO[].class);

    assertEquals(1, response.length);

    var course = response[0];
    assertEquals(course.id(), TestData.COURSE.id());
    assertEquals(course.name(), TestData.COURSE.name());
    assertEquals(course.par(), TestData.COURSE.par());
    assertEquals(course.holes(), TestData.COURSE.holes().size());
  }

  @Test
  void should_get_course_by_id() {
    var response = given()
        .when()
        .get(ENDPOINT + "/1")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .extract()
        .as(CourseDTO.class);

    assertEquals(response.id(), TestData.COURSE.id());
    assertEquals(response.name(), TestData.COURSE.name());
    assertEquals(response.par(), TestData.COURSE.par());
    assertEquals(response.holes().size(), TestData.COURSE.holes().size());
  }

  @Disabled("Mock data never returns 404")
  @Test
  void should_handle_course_not_found() {
    given()
        .when()
        .get(ENDPOINT + "/99")
        .then()
        .statusCode(404)
        .contentType(ContentType.JSON);
  }

}
